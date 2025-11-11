package com.tennistime.reservation.infrastructure.payment.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payload submitted to SEP in order to verify a transaction.
 */
public record SepVerifyRequest(
        @JsonProperty("RefNum") String referenceNumber,
        @JsonProperty("TerminalNumber") String terminalNumber
) {
}
