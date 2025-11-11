package com.tennistime.reservation.application.dto.payment;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request body accepted by the create payment endpoint.
 */
public class PaymentInitiationRequest {

    @NotNull
    private UUID reservationId;

    @NotNull
    @jakarta.validation.constraints.DecimalMin("1")
    private BigDecimal amount;

    private String customerCellNumber;

    private Integer tokenExpiryInMinutes;

    private String hashedCardNumber;

    private Boolean useGetMethod;

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCustomerCellNumber() {
        return customerCellNumber;
    }

    public void setCustomerCellNumber(String customerCellNumber) {
        this.customerCellNumber = customerCellNumber;
    }

    public Integer getTokenExpiryInMinutes() {
        return tokenExpiryInMinutes;
    }

    public void setTokenExpiryInMinutes(Integer tokenExpiryInMinutes) {
        this.tokenExpiryInMinutes = tokenExpiryInMinutes;
    }

    public String getHashedCardNumber() {
        return hashedCardNumber;
    }

    public void setHashedCardNumber(String hashedCardNumber) {
        this.hashedCardNumber = hashedCardNumber;
    }

    public Boolean getUseGetMethod() {
        return useGetMethod;
    }

    public void setUseGetMethod(Boolean useGetMethod) {
        this.useGetMethod = useGetMethod;
    }
}
