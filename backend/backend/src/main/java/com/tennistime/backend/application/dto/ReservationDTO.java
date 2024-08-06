package com.tennistime.backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    @Schema(hidden = true)
    private Long id;

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

    @Schema(example = "1")
    private Long userProfileId;

    @Schema(example = "1")
    private Long courtId;
}
