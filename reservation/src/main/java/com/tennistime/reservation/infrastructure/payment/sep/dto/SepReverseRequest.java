package com.tennistime.reservation.infrastructure.payment.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payload submitted to SEP to reverse a previously verified transaction.
 */
public record SepReverseRequest(
        @JsonProperty("RefNum") String referenceNumber,
        @JsonProperty("TerminalNumber") String terminalNumber
) {
}
