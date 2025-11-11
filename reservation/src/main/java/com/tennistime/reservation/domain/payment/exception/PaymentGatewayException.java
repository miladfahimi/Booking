package com.tennistime.reservation.domain.payment.exception;

/**
 * Exception thrown when the bank gateway cannot process a payment request or returns an
 * unexpected response.
 */
public class PaymentGatewayException extends RuntimeException {

    /**
     * Creates the exception with the provided message.
     *
     * @param message human readable description of the gateway failure.
     */
    public PaymentGatewayException(String message) {
        super(message);
    }

    /**
     * Creates the exception with the provided message and root cause.
     *
     * @param message message describing the failure.
     * @param cause   underlying exception.
     */
    public PaymentGatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}
