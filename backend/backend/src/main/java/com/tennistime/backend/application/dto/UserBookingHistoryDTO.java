package com.tennistime.backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Booking History Data Transfer Object")
public class UserBookingHistoryDTO {

    @Schema(description = "Unique identifier of the user booking history", example = "1")
    private Long id;

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long userId;

    @Schema(description = "Booking date of the user", example = "2024-01-01")
    private String bookingDate = "Does not filled by User";

    @Schema(description = "Status of the booking", example = "Completed")
    private String status = "Does not filled by User";

    @Schema(description = "Court ID of the booking", example = "2")
    private Long courtId = null;

    @Schema(description = "Booking date in Persian calendar", example = "1402-10-11")
    private String bookingDatePersian = "Does not filled by User";
}
