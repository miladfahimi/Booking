package com.tennistime.bff.domain.model.types;

/**
 * Mirror of the reservation service status values to keep the BFF aligned with the source of truth.
 */
public enum ReservationStatus {
    AVAILABLE,
    /**
     * Marks a slot that currently lives only inside a customer's basket.
     */
    IN_BASKET,
    /**
     * Indicates that a checkout flow has started and the slot should be blocked system-wide.
     */
    PENDING,
    CONFIRMED,
    CANCELED,
    EXPIRED,
    MAINTENANCE,
    ADMIN_HOLD
}