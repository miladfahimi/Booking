package com.tennistime.reservation.application.dto;

import com.tennistime.reservation.domain.model.types.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBookingHistoryUpdateRequest {

    private UUID userId;
    private UUID reservationId;
    private UUID serviceId;
    private String bookingDate;
    private ReservationStatus status;
    private String bookingDatePersian;
    private boolean isItArchived;
    private boolean isUserNotified;
    private String notes;
}
