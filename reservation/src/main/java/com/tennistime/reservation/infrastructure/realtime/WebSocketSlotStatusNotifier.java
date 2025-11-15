package com.tennistime.reservation.infrastructure.realtime;

import com.tennistime.reservation.application.notification.SlotStatusNotifier;
import com.tennistime.reservation.domain.notification.SlotStatusNotification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * WebSocket backed implementation of {@link SlotStatusNotifier}.
 */
@Component
@RequiredArgsConstructor
public class WebSocketSlotStatusNotifier implements SlotStatusNotifier {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketSlotStatusNotifier.class);

    private final SimpMessagingTemplate messagingTemplate;

    /** {@inheritDoc} */
    @Override
    public void publish(SlotStatusNotification notification) {
        logger.debug("Broadcasting slot {} status {}", notification.getCompositeSlotId(), notification.getStatus());
        messagingTemplate.convertAndSend("/topic/slot-status", notification);
    }
}
