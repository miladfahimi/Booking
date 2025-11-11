package com.tennistime.reservation.infrastructure.payment.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response returned after verifying a transaction with SEP.
 */
public record SepVerifyResponse(
        @JsonProperty("Success") boolean success,
        @JsonProperty("ResultCode") String resultCode,
        @JsonProperty("ResultDescription") String resultDescription,
        @JsonProperty("TransactionDetail") TransactionDetail transactionDetail
) {
    /**
     * Carries the financial information of the verified transaction.
     */
    public record TransactionDetail(
            @JsonProperty("OrginalAmount") long originalAmount,
            @JsonProperty("AffectiveAmount") long affectiveAmount,
            @JsonProperty("RRN") String rrn,
            @JsonProperty("MaskedPan") String maskedPan
    ) {
    }
}
