package com.tennistime.reservation.application.dto;

import com.tennistime.reservation.domain.model.types.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Data transfer object representing a basket entry stored for a customer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBasketItemDTO {

    @Schema(description = "Identifier of the basket item", example = "c3b23d49-5f83-4ceb-b6b9-1ceae5e5d4b9")
    private UUID id;

    @Schema(description = "Identifier of the user owning the basket", example = "769df378-2cf1-411f-b5f5-0c69cb7d4016")
    private UUID userId;

    @Schema(description = "Identifier of the related provider", example = "f6ecc0c6-8d98-40cb-8f9e-d80de5bfd4c9")
    private UUID providerId;

    @Schema(description = "Identifier of the selected service", example = "13dd0543-ab7a-428d-a6a3-5d6ade751e9f")
    private UUID serviceId;

    @Schema(description = "Readable service name", example = "Court 1 Morning Session")
    private String serviceName;

    @Schema(description = "Unique identifier of the chosen slot", example = "slot-3")
    private String slotId;

    @Schema(description = "Reservation date in Gregorian calendar", example = "2024-08-05")
    private LocalDate reservationDate;

    @Schema(description = "Reservation date in Persian calendar", example = "1403-05-15")
    private String reservationDatePersian;

    @Schema(description = "Slot start time", example = "09:00")
    private LocalTime startTime;

    @Schema(description = "Slot end time", example = "10:00")
    private LocalTime endTime;

    @Schema(description = "Price associated with the slot", example = "350000")
    private BigDecimal price;

    @Schema(description = "Length of the slot in minutes", example = "60")
    private Integer durationMinutes;

    @Schema(description = "Current status of the basket entry", example = "IN_BASKET")
    private ReservationStatus status;
}
