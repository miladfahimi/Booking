package com.tennistime.bff.application.dto;

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
     * The status of the slot, indicating availability (e.g., "available", "booked").
     */
    private String status;

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
