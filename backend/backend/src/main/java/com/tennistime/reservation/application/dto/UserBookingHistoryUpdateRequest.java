package com.tennistime.reservation.application.dto;

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
    private String bookingDate;  // Ensure this is in ISO_LOCAL_DATE_TIME format
    private String status;
    private String bookingDatePersian;
    private boolean isItArchived;
    private boolean isUserNotified;
    private String notes;
}
