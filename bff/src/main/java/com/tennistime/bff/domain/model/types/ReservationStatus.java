package com.tennistime.bff.domain.model.types;

/**
 * Mirror of the reservation service status values to keep the BFF aligned with the source of truth.
 */
public enum ReservationStatus {
    AVAILABLE,
    PENDING,
    CONFIRMED,
    CANCELED,
    EXPIRED,
    MAINTENANCE,
    ADMIN_HOLD
}