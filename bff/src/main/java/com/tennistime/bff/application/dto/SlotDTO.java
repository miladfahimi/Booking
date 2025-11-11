package com.tennistime.bff.application.dto;

import com.tennistime.bff.domain.model.types.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing a time slot within a service.
 * Includes details such as the time, status, pricing, capacity, and reservation information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotDTO {

    /**
     * Unique identifier for the slot.
     */
    private String slotId;

    /**
     * The time this slot starts, formatted as HH:mm (e.g., "09:00").
     */
    private String time;

    /**
     * The time this slot ends, formatted as HH:mm (e.g., "09:30").
     */
    private String endTime;

    /**
     * The status of the slot, mirroring {@link ReservationStatus} values (e.g., AVAILABLE, CONFIRMED).
     */
    private ReservationStatus status;

    /**
     * The price for reserving this slot.
     */
    private BigDecimal price;

    /**
     * The maximum capacity for this slot, indicating how many users can reserve it.
     */
    private int capacity;

    /**
     * The ID of the user who reserved this slot, or null if the slot is not booked.
     */
    private String reservedBy;
}
