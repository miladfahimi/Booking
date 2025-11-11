package com.tennistime.reservation.infrastructure.payment.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response returned by SEP upon requesting a payment token.
 */
public record SepTokenResponse(
        @JsonProperty("status") int status,
        @JsonProperty("token") String token,
        @JsonProperty("errorCode") Integer errorCode,
        @JsonProperty("errorDesc") String errorDescription
) {
    /**
     * Indicates whether the token request was successful.
     *
     * @return {@code true} when {@code status} equals {@code 1}.
     */
    public boolean isSuccessful() {
        return status == 1 && token != null && !token.isBlank();
    }
}
