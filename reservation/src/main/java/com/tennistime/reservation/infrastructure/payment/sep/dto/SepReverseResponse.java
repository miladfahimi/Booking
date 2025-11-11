package com.tennistime.reservation.infrastructure.payment.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response returned by SEP when attempting to reverse a transaction.
 */
public record SepReverseResponse(
        @JsonProperty("Success") boolean success,
        @JsonProperty("ResultCode") String resultCode,
        @JsonProperty("ResultDescription") String resultDescription
) {
}
