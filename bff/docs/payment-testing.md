# Realtime slot status notifications

This document describes the realtime pipeline that keeps slot availability synchronized across clients and outlines the steps required to migrate the flow to a dedicated notification service when the number of notifications grows.

## Current architecture

The realtime implementation is intentionally isolated inside dedicated packages so that it can be replaced without touching unrelated modules.

### Reservation service

* `ReservationWebSocketConfig` enables the Spring messaging broker, exposes `/ws/reservation`, and configures `/topic/**` as brokered destinations so that downstream bridges can subscribe with plain STOMP.【F:reservation/src/main/java/com/tennistime/reservation/infrastructure/realtime/ReservationWebSocketConfig.java†L1-L28】
* All domain services send updates through the `SlotStatusNotifier` interface; the default implementation (`WebSocketSlotStatusNotifier`) simply calls `SimpMessagingTemplate.convertAndSend("/topic/slot-status", notification)` to broadcast payloads produced by the services.【F:reservation/src/main/java/com/tennistime/reservation/infrastructure/realtime/WebSocketSlotStatusNotifier.java†L3-L28】
* `ReservationBasketService` is responsible for emitting `IN_BASKET`, `PENDING`, and `AVAILABLE` events whenever a basket item is added, updated, or cleared. It builds `SlotStatusNotification` objects with both the composite slot id (service + slot) and the stripped slot id so consumers can match either format.【F:reservation/src/main/java/com/tennistime/reservation/application/service/ReservationBasketService.java†L55-L157】
* `ReservationService` emits `PENDING`, `CONFIRMED`, and other final reservation statuses after persistence operations so the rest of the system receives the definitive availability state for each slot.【F:reservation/src/main/java/com/tennistime/reservation/application/service/ReservationService.java†L77-L337】

### BFF

* `BffWebSocketConfig` mirrors the reservation WebSocket configuration, exposing `/ws/bff` for browsers while brokering outbound messages on `/topic/slot-status`. This keeps the frontend shielded from the reservation service credentials or topology.【F:bff/src/main/java/com/tennistime/bff/infrastructure/realtime/BffWebSocketConfig.java†L1-L26】
* `RealtimeProperties` records the upstream reservation endpoint/topic and the downstream BFF topic, allowing deployments to override URLs without code changes.【F:bff/src/main/java/com/tennistime/bff/infrastructure/realtime/RealtimeProperties.java†L1-L30】
* `RealtimeClientConfig` wires a resilient `WebSocketStompClient` (plus message converter and scheduler) that the bridge component uses to subscribe to the reservation topic.【F:bff/src/main/java/com/tennistime/bff/infrastructure/realtime/RealtimeClientConfig.java†L1-L65】
* `ReservationSlotStatusBridge` connects to `/ws/reservation`, subscribes to `/topic/slot-status`, and republished every `SlotStatusNotification` to the local `/topic/slot-status` endpoint exposed by the BFF. The bridge also re-establishes the upstream session when transport errors occur.【F:bff/src/main/java/com/tennistime/bff/infrastructure/realtime/ReservationSlotStatusBridge.java†L1-L92】

### Frontend

* `SlotStatusRealtimeService` opens a raw WebSocket to `/api/v1/ws/bff`, issues STOMP `CONNECT`/`SUBSCRIBE` frames, parses each JSON body into a `SlotStatusNotification`, and emits the value to RxJS subscribers. Automatic reconnects ensure that temporary outages do not break the UI stream.【F:frontend/tennis-time/libs/reservation/src/lib/reservation/realtime/slot-status-realtime.service.ts†L1-L154】
* `ReservationEffects` wires the service into NgRx by turning every realtime update into a `slotStatusUpdated` action when the effects class is constructed, guaranteeing that consumers start listening as soon as the feature module loads.【F:frontend/tennis-time/libs/reservation/src/lib/reservation/store/reservation.effects.ts†L1-L140】
* `reservation.reducer` reconciles the updates by locating the matching slot (by service id + slot id) and replacing its `status`. The UI instantly re-renders because selectors read from the `slotsByService` dictionary.【F:frontend/tennis-time/libs/reservation/src/lib/reservation/store/reservation.reducer.ts†L208-L234】

These layers deliberately hide transport details from the domain so that the notifier can be swapped for a message broker or another protocol without touching controllers or UI state management.

## Slot lifecycle and example flow

The domain uses the `ReservationStatus` enum to describe every state a slot can be in. Key stages are `AVAILABLE`, `IN_BASKET`, `PENDING`, and `CONFIRMED`, while `CANCELED`, `EXPIRED`, `MAINTENANCE`, and `ADMIN_HOLD` are reserved for administrative flows.【F:reservation/src/main/java/com/tennistime/reservation/domain/model/types/ReservationStatus.java†L8-L37】 The following illustrates how those states propagate through the realtime stack:

1. **User picks a slot (IN_BASKET):** `ReservationBasketService.addOrUpdateItem` sets the basket entry to `IN_BASKET` and publishes a notification so every client knows the slot is temporarily locked for that user.【F:reservation/src/main/java/com/tennistime/reservation/application/service/ReservationBasketService.java†L55-L142】
2. **Checkout begins (PENDING):** The frontend calls the basket status endpoint, which delegates to `ReservationBasketService.updateStatus`. This method saves the new status (typically `PENDING`) and emits another notification, indicating that payment has started and the slot should appear blocked for everyone.【F:reservation/src/main/java/com/tennistime/reservation/application/service/ReservationBasketService.java†L117-L142】
3. **Payment succeeds (CONFIRMED):** During checkout the client uses `ReservationService` APIs to create reservations (`save`, `saveAll`) which start as `PENDING` and then invokes `updateReservationStatus` (e.g., via `ReservationCheckoutService.confirmReservations`) to mark them `CONFIRMED`. Each persistence call triggers `notifyStatusChange`, which broadcasts the final state through the notifier.【F:reservation/src/main/java/com/tennistime/reservation/application/service/ReservationService.java†L77-L337】【F:frontend/tennis-time/libs/reservation/src/lib/reservation/services/reservation-checkout.service.ts†L91-L123】
4. **User abandons checkout (AVAILABLE):** Removing a basket entry or clearing the basket calls `notifyStatusChange(..., ReservationStatus.AVAILABLE)`, releasing the slot for others. Likewise, cancellation flows inside `ReservationService` produce the relevant status transitions.【F:reservation/src/main/java/com/tennistime/reservation/application/service/ReservationBasketService.java†L84-L142】【F:reservation/src/main/java/com/tennistime/reservation/application/service/ReservationService.java†L122-L153】

Because every state change goes through the same notifier interface, additional transitions (e.g., `EXPIRED`) automatically feed the realtime channel without touching the transport layer.

## Slot status payload contract

All layers exchange the `SlotStatusNotification` DTO, which carries both the `slotId` and `compositeSlotId`, the owning `serviceId`, the originating `reservationDate`, the identifier of the user who triggered the change, and the latest `ReservationStatus`. The composite identifier (service + slot) lets consumers disambiguate identical slot ids served by different services, `reservationDate` keeps clients from applying updates to a different day when time ranges overlap, and `userId` allows receivers to distinguish between the current user’s basket updates and other customers’ holds.【F:reservation/src/main/java/com/tennistime/reservation/domain/notification/SlotStatusNotification.java†L12-L26】【F:bff/src/main/java/com/tennistime/bff/domain/realtime/SlotStatusNotification.java†L1-L24】 When migrating to another notifier implementation, keep this payload stable so that the frontend continues to work without recompilation.

## Configuration

## Configuration

Default connection settings live in the following properties:

```
realtime.reservation.url=ws://localhost:8085/ws/reservation
realtime.reservation.topic=/topic/slot-status
realtime.bff.topic=/topic/slot-status
```

Frontend builds use `environment.slotStatusWebSocketUrl` and `environment.slotStatusTopic`. Override the variables per environment (for example via `RESERVATION_REALTIME_URL`) to point to deployed services.

## Migrating to a dedicated notification service

1. **Introduce a message broker:** Replace `WebSocketSlotStatusNotifier` with a publisher that writes to a broker (Kafka, Redis Streams, etc.) but continue to expose it through `SlotStatusNotifier`. No other class should need to know whether messages go through STOMP or a broker.【F:reservation/src/main/java/com/tennistime/reservation/infrastructure/realtime/WebSocketSlotStatusNotifier.java†L3-L28】
2. **Add a broker consumer inside the BFF (temporary):** Instead of connecting directly to `/ws/reservation`, create a consumer component next to `ReservationSlotStatusBridge` that subscribes to the broker topic and forwards updates to `/topic/slot-status`. This allows you to verify the new delivery mechanism before standing up a new app.【F:bff/src/main/java/com/tennistime/bff/infrastructure/realtime/ReservationSlotStatusBridge.java†L1-L92】
3. **Spin up the notification service:** Move the WebSocket endpoint currently configured by `BffWebSocketConfig` into the new application and let it subscribe to the broker topic. Keep the STOMP topic and payload identical so the frontend does not notice the change.【F:bff/src/main/java/com/tennistime/bff/infrastructure/realtime/BffWebSocketConfig.java†L1-L26】
4. **Repoint the frontend:** Update `slotStatusWebSocketUrl` to the notification service domain. Because the topic remains `/topic/slot-status`, the client reconnect logic in `SlotStatusRealtimeService` keeps working as-is.【F:frontend/tennis-time/libs/reservation/src/lib/reservation/realtime/slot-status-realtime.service.ts†L19-L154】
5. **Retire the bridge:** Once the new service proves stable, delete `ReservationSlotStatusBridge`, the `RealtimeClientConfig` beans, and the old WebSocket endpoint from the BFF. The reservation app continues to emit events via `SlotStatusNotifier`, but they now land on the broker instead of the embedded WebSocket broker.

By following this roadmap, you can split realtime delivery into an independent service without rewriting basket logic, reservation flows, or the Angular client.