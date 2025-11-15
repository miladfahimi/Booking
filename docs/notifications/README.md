# Realtime slot status notifications

This document describes the minimal realtime pipeline added to keep slot availability synchronized across clients and outlines the steps required to migrate the flow to a dedicated notification service when needed.

## Current architecture

The realtime implementation is intentionally isolated inside dedicated packages so that it can be replaced without touching unrelated modules.

### Reservation service

* `com.tennistime.reservation.infrastructure.realtime.ReservationWebSocketConfig` exposes a STOMP endpoint at `/ws/reservation` and brokers messages on `/topic/slot-status`.
* `com.tennistime.reservation.application.notification.SlotStatusNotifier` abstracts slot status broadcasting.
* `com.tennistime.reservation.infrastructure.realtime.WebSocketSlotStatusNotifier` implements the notifier through `SimpMessagingTemplate`.
* `ReservationBasketService` resolves slot identifiers, builds `SlotStatusNotification` payloads, and publishes them whenever basket entries are created, updated, or deleted.

### BFF

* `com.tennistime.bff.infrastructure.realtime.BffWebSocketConfig` exposes the `/ws/bff` endpoint and republishes messages on `/topic/slot-status`.
* `RealtimeProperties` loads the upstream reservation endpoint and downstream topic from configuration.
* `RealtimeClientConfig` wires a STOMP client with heartbeats and Jackson serialization.
* `ReservationSlotStatusBridge` subscribes to the reservation topic and forwards each `SlotStatusNotification` to connected frontend clients via the BFF broker.

### Frontend

* `SlotStatusRealtimeService` opens a SockJS/STOMP connection to the BFF endpoint, emitting parsed `SlotStatusUpdate` objects.
* `ReservationEffects.slotStatusUpdates$` listens to the service stream and dispatches `slotStatusUpdated` actions.
* The reducer applies updates to `slotsByService` and basket state so UI components instantly reflect remote changes.

## Configuration

Default connection settings live in the following properties:

```
realtime.reservation.url=ws://localhost:8085/ws/reservation
realtime.reservation.topic=/topic/slot-status
realtime.bff.topic=/topic/slot-status
```

Frontend builds use `environment.slotStatusWebSocketUrl` and `environment.slotStatusTopic`. Override the variables per environment (for example via `RESERVATION_REALTIME_URL`) to point to deployed services.

## Migrating to a dedicated notification service

1. **Introduce a message broker:** Replace `WebSocketSlotStatusNotifier` with a publisher that writes to a broker (e.g., Kafka or Redis Streams). Keep the `SlotStatusNotifier` interface intact so `ReservationBasketService` remains unchanged.
2. **Deploy the notification service:** Move the WebSocket endpoint from the BFF into the new service. Subscribe to the broker topic and reuse the `SlotStatusNotification` payload contract.
3. **Update configuration:** Point `RealtimeProperties.reservation.url` (or a new property) to the broker consumer endpoint exposed by the notification service. Remove the direct reservation connection from the BFF once validation completes.
4. **Adjust the frontend endpoint:** Switch `slotStatusWebSocketUrl` to the new notification service domain. The STOMP topic (`/topic/slot-status`) can be reused as long as the new service mirrors the payload structure.
5. **Decommission temporary bridge:** After traffic is verified on the new path, delete `ReservationSlotStatusBridge` and the associated configuration classes. The reservation application keeps emitting notifications through the interface, now targeting the broker.

The separation of concerns introduced here keeps domain services unaware of delivery specifics and lets you swap transport layers without refactoring controllers or user flows.
