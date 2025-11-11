package com.tennistime.reservation.domain.payment;

/**
 * Represents the lifecycle states that a payment may traverse while interacting with the
 * acquiring bank gateway.
 */
public enum PaymentStatus {
    /** Payment aggregate created but the bank token was not yet requested. */
    CREATED,
    /** Bank token has been issued and the customer can be redirected to the gateway. */
    TOKEN_ISSUED,
    /** Callback payload from the bank has been received and stored. */
    CALLBACK_RECEIVED,
    /** Payment has been successfully verified with the bank. */
    VERIFIED,
    /** Payment verification failed or was rejected by the bank. */
    FAILED,
    /** Payment has been reversed after a successful verification. */
    REVERSED
}
