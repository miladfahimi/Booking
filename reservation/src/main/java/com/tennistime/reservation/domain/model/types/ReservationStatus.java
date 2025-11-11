package com.tennistime.reservation.domain.model.types;

import java.util.Arrays;

/**
 * Enumerates the lifecycle states a reservation can be in.
 */
public enum ReservationStatus {
    AVAILABLE,
    PENDING,
    CONFIRMED,
    CANCELED,
    EXPIRED,
    MAINTENANCE,
    ADMIN_HOLD;


    /**
     * Resolve a {@link ReservationStatus} from a case-insensitive string representation.
     *
     * @param value textual status received from a client or external system
     * @return the matching {@link ReservationStatus}
     * @throws IllegalArgumentException when the provided value does not match any known status
     */
    public static ReservationStatus fromString(String value) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported reservation status: " + value));
    }
}