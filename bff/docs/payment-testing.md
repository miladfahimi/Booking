# Testing the BFF payment flow in a local development environment

The BFF proxies every payment-related operation to the reservation service. In the development profile the reservation service ships with `reservation.payment.sep.mock-enabled=true`, so no external SEP gateway is contacted and the entire flow can be exercised locally.【F:reservation/src/main/resources/application-dev.properties†L45-L53】【F:reservation/src/main/java/com/tennistime/reservation/application/service/PaymentService.java†L51-L90】【F:reservation/src/main/java/com/tennistime/reservation/application/service/PaymentService.java†L111-L139】

Follow the steps below to run end-to-end tests against the BFF while the mock is enabled.

## 1. Start the stack

From the repository root start the docker-compose development environment. This brings up PostgreSQL, Redis, the Authentication service, the Reservation service, and the BFF on their default ports.【F:README.md†L43-L92】

```bash
docker-compose -f devops/docker-compose.dev.yml up --build
```

Keep the stack running in a separate terminal.

## 2. Obtain a JWT access token

Authentication endpoints are exposed at `http://localhost:8082/api/v1/auth`. The database is seeded with several demo users whose passwords are all `123` (bcrypt-hashed in `data.sql`).【F:authentication/src/main/resources/data.sql†L1-L8】

Request a token by calling the sign-in endpoint with one of the demo accounts, for example `john_doe@example.com`:

```bash
curl -X POST \
  http://localhost:8082/api/v1/auth/signin \
  -H 'Content-Type: application/json' \
  -d '{
        "email": "john_doe@example.com",
        "password": "123",
        "deviceModel": "local-cli",
        "os": "linux",
        "browser": "curl"
      }'
```

The response payload contains a `token` field. Store it for the next calls (e.g. `export JWT="<token-value>"`).

## 3. Pick a reservation to pay for

The reservation database is pre-populated; you can fetch the list via the BFF at `GET http://localhost:8083/api/v1/portal/user/reservations`.【F:bff/src/main/java/com/tennistime/bff/application/controller/UserPortalController.java†L25-L34】 Use the JWT from the previous step:

```bash
curl -H "Authorization: Bearer $JWT" \
  http://localhost:8083/api/v1/portal/user/reservations | jq '.[0].id'
```

Choose any reservation `id` from the output for the payment initiation request.

## 4. Initiate a payment via the BFF

Call the BFF initiation endpoint (`POST /portal/user/payments/create`) with the reservation id and amount. In mock mode the reservation service returns a fake token and redirect URL that you can verify immediately.【F:bff/src/main/java/com/tennistime/bff/application/controller/PaymentController.java†L33-L44】【F:reservation/src/main/java/com/tennistime/reservation/application/service/PaymentService.java†L60-L90】

```bash
curl -X POST \
  http://localhost:8083/api/v1/portal/user/payments/create \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $JWT" \
  -d '{
        "reservationId": "<reservation-uuid>",
        "amount": 150000
      }'
```

The JSON response contains:
- `paymentId`: identifier of the created payment aggregate.
- `token`: starts with `MOCK-` when the mock gateway is active.
- `referenceNumber`: needed for the callback simulation.
- `redirectUrl`: SEP landing page generated from the token.

Export the `paymentId` and `referenceNumber` for the next steps.

## 5. Simulate the SEP callback

When the gateway mock is enabled, any callback that echoes the `resNum` (reference number) immediately marks the payment as verified.【F:bff/src/main/java/com/tennistime/bff/application/controller/PaymentController.java†L46-L54】【F:reservation/src/main/java/com/tennistime/reservation/application/service/PaymentService.java†L111-L139】

```bash
curl -X POST \
  http://localhost:8083/api/v1/portal/user/payments/callback \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $JWT" \
  -d '{
        "state": "OK",
        "status": "0",
        "refNum": "<reference-number>",
        "resNum": "<reference-number>",
        "rrn": "123456789012",
        "traceNo": "654321",
        "amount": 150000,
        "terminalId": "TENNIS-TIME"
      }'
```

The response includes `success: true` and the mock verification message.

## 6. Reverse the payment (optional)

Finally, call the reversal endpoint with the `paymentId`. In mock mode the reservation service immediately marks the payment as reversed and returns a confirmation timestamp.【F:bff/src/main/java/com/tennistime/bff/application/controller/PaymentController.java†L56-L65】【F:reservation/src/main/java/com/tennistime/reservation/application/service/PaymentService.java†L141-L180】

```bash
curl -X POST \
  http://localhost:8083/api/v1/portal/user/payments/<paymentId>/reverse \
  -H "Authorization: Bearer $JWT"
```

You should receive `{ "reversed": true, "message": "Reverse transaction completed in mock mode", ... }`, confirming the full flow works end-to-end without reaching the real SEP gateway.
