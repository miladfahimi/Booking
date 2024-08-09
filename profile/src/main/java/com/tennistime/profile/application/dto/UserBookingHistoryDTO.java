package com.tennistime.profile.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Booking History Data Transfer Object")
public class UserBookingHistoryDTO {

    @Schema(description = "Unique identifier of the user booking history", example = "1")
    private UUID id;

    @Schema(description = "Unique identifier of the user", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID userId;

    @Schema(description = "Unique identifier of the reservation", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID reservationId;

    @Schema(description = "Unique identifier of the court", example = "2")
    private Long courtId;

    @Schema(description = "Booking date of the user", example = "2024-01-01T10:15:30")
    private String bookingDate;

    @Schema(description = "Status of the booking", example = "Completed")
    private String status;

    @Schema(description = "Booking date in Persian calendar", example = "1402-10-11")
    private String bookingDatePersian;

    @Schema(description = "Indicates if the booking is archived", example = "false")
    private boolean isItArchived;

    @Schema(description = "Indicates if the user has been notified", example = "false")
    private boolean isUserNotified;

    @Schema(description = "Additional notes for the booking", example = "User requested an early check-in.")
    private String notes;

    @Schema(description = "Creation timestamp", example = "2024-01-01T10:15:30")
    private String createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-02T12:20:10")
    private String updatedAt;
}
