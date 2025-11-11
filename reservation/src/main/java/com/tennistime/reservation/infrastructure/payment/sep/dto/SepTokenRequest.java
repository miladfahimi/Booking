package com.tennistime.reservation.infrastructure.payment.sep.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payload sent to SEP for requesting a payment token.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SepTokenRequest(
        @JsonProperty("action") String action,
        @JsonProperty("TerminalId") String terminalId,
        @JsonProperty("Amount") long amount,
        @JsonProperty("ResNum") String referenceNumber,
        @JsonProperty("RedirectUrl") String redirectUrl,
        @JsonProperty("CellNumber") String cellNumber,
        @JsonProperty("TokenExpiryInMin") Integer tokenExpiryInMinutes,
        @JsonProperty("HashedCardNumber") String hashedCardNumber,
        @JsonProperty("GetMethod") Boolean getMethod
) {
}
