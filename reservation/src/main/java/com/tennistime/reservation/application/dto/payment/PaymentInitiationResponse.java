package com.tennistime.reservation.application.dto.payment;

import java.net.URI;
import java.util.UUID;

/**
 * Response payload returned after requesting a payment token.
 */
public class PaymentInitiationResponse {

    private UUID paymentId;
    private String token;
    private URI redirectUrl;
    private String referenceNumber;

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public URI getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(URI redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
