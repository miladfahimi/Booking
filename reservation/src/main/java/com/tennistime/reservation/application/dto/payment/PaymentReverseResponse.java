package com.tennistime.reservation.application.dto.payment;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response returned to operators when a reverse transaction attempt is made.
 */
public class PaymentReverseResponse {

    private UUID paymentId;
    private boolean reversed;
    private String message;
    private LocalDateTime reversedAt;

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getReversedAt() {
        return reversedAt;
    }

    public void setReversedAt(LocalDateTime reversedAt) {
        this.reversedAt = reversedAt;
    }
}
