package com.tennistime.backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    @Schema(hidden = true)
    private UUID id; // Updated from Long to UUID

    @Schema(example = "2024-08-05")
    private LocalDate reservationDate; // Changed to LocalDate

    @Schema(example = "1403-04-01")
    private String reservationDatePersian;

    @Schema(example = "10:00")
    private LocalTime startTime;

    @Schema(example = "12:00")
    private LocalTime endTime;

    @Schema(example = "confirmed")
    private String status;

    @Schema(example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID userId;

    @Schema(example = "d290f1ee-6c54-4b01-90e6-d70173333331")
    private UUID courtId; // Ensure this is set before creating/updating a reservation
}
