package com.tennistime.reservation.application.notification;

import com.tennistime.reservation.domain.notification.SlotStatusNotification;

/**
 * Publishes slot status updates to subscribed consumers.
 */
public interface SlotStatusNotifier {

    /**
     * Broadcasts the provided slot status change to downstream subscribers.
     *
     * @param notification payload describing the slot transition
     */
    void publish(SlotStatusNotification notification);
}
