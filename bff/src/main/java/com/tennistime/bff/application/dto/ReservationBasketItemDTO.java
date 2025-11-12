package com.tennistime.bff.application.dto;

import com.tennistime.bff.domain.model.types.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * DTO exposing basket item data through the BFF.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBasketItemDTO {

    private UUID id;
    private UUID userId;
    private UUID providerId;
    private UUID serviceId;
    private String serviceName;
    private String slotId;
    private LocalDate reservationDate;
    private String reservationDatePersian;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal price;
    private Integer durationMinutes;
    private ReservationStatus status;
}
