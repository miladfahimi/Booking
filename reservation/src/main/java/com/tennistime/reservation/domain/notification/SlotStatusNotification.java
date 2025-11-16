package com.tennistime.reservation.domain.notification;

import com.tennistime.reservation.domain.model.types.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Immutable representation of a slot status update broadcast to external subscribers.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotStatusNotification {

    private String slotId;
    private String compositeSlotId;
    private UUID serviceId;
    private ReservationStatus status;
    private String reservationDate;
}
