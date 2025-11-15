package com.tennistime.bff.infrastructure.realtime;

import com.tennistime.bff.domain.realtime.SlotStatusNotification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

/**
 * Bridges slot status updates from the reservation service to BFF subscribers.
 */
@Component
@RequiredArgsConstructor
public class ReservationSlotStatusBridge {

    private static final Logger logger = LoggerFactory.getLogger(ReservationSlotStatusBridge.class);

    private final WebSocketStompClient stompClient;
    private final RealtimeProperties realtimeProperties;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Initiates the connection once the application is fully bootstrapped.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void connect() {
        logger.info("\033[1;35m[SlotBridge]\033[0m Initializing connection to reservation realtime endpoint at {}", realtimeProperties.getReservation().getUrl());
        openSession();
    }

    /**
     * Opens a new STOMP session to the reservation service.
     */
    private void openSession() {
        try {
            logger.info("\033[1;35m[SlotBridge]\033[0m Opening STOMP session to {}", realtimeProperties.getReservation().getUrl());
            stompClient.connectAsync(realtimeProperties.getReservation().getUrl(), new SlotStatusSessionHandler());
        } catch (Exception ex) {
            logger.error("\033[1;31m[SlotBridge]\033[0m Failed to initiate STOMP connection: {}", ex.getMessage(), ex);
        }
    }

    private class SlotStatusSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            logger.info("\033[1;32m[SlotBridge]\033[0m Connected to reservation realtime endpoint, sessionId={}", session.getSessionId());
            logger.info("\033[1;32m[SlotBridge]\033[0m Subscribing to reservation topic {}", realtimeProperties.getReservation().getTopic());
            session.subscribe(realtimeProperties.getReservation().getTopic(), new SlotStatusFrameHandler());
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            logger.warn("\033[1;31m[SlotBridge]\033[0m Transport error in reservation bridge: {}", exception.getMessage(), exception);
            logger.info("\033[1;33m[SlotBridge]\033[0m Scheduling reconnect to reservation realtime endpoint");
            openSession();
        }
    }

    private class SlotStatusFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return SlotStatusNotification.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            if (payload instanceof SlotStatusNotification notification) {
                logger.info(
                        "\033[1;36m[SlotBridge]\033[0m Received slot status update: compositeSlotId={}, status={}, serviceId={}",
                        notification.getCompositeSlotId(),
                        notification.getStatus(),
                        notification.getServiceId()
                );
                messagingTemplate.convertAndSend(realtimeProperties.getBff().getTopic(), notification);
                logger.debug("\033[0;36m[SlotBridge]\033[0m Forwarded slot update to BFF topic {}", realtimeProperties.getBff().getTopic());
            } else {
                logger.warn("\033[1;31m[SlotBridge]\033[0m Unexpected payload type received from reservation: {}", payload != null ? payload.getClass() : "null");
            }
        }
    }
}
