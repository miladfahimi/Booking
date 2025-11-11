package com.tennistime.reservation.domain.payment.exception;

/**
 * Exception thrown when a payment aggregate cannot be located using the provided identifiers.
 */
public class PaymentNotFoundException extends RuntimeException {

    /**
     * Creates an exception with the provided message.
     *
     * @param message human readable description of the missing payment.
     */
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
