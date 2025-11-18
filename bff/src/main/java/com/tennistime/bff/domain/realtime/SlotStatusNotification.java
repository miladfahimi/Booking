package com.tennistime.bff.domain.realtime;

import com.tennistime.bff.domain.model.types.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data structure carrying slot status information received from upstream services.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotStatusNotification {

    private String slotId;
    private String compositeSlotId;
    private String serviceId;
    private ReservationStatus status;
    private String reservationDate;
    private String userId;
    private SlotBasketStateNotification basketState;
}