package com.tennistime.reservation.infrastructure.payment.sep;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Configuration properties describing the SEP integration endpoints and static parameters.
 */
@Component
@ConfigurationProperties(prefix = "reservation.payment.sep")
public record SepPaymentProperties(
        @DefaultValue("https://sep.shaparak.ir/onlinepg/onlinepg") URI tokenEndpoint,
        @DefaultValue("https://sep.shaparak.ir/OnlinePG/SendToken") URI paymentPage,
        @DefaultValue("https://sep.shaparak.ir/verifyTxnRandomSessionkey/ipg/VerifyTransaction") URI verifyEndpoint,
        @DefaultValue("https://sep.shaparak.ir/verifyTxnRandomSessionkey/ipg/ReverseTransaction") URI reverseEndpoint,
        String terminalId,
        URI callbackUrl,
        @DefaultValue("20") Integer defaultTokenExpiryMinutes,
        @DefaultValue("false") Boolean redirectWithGet
) {
    /**
     * Builds the redirect URL for the end user based on the returned token.
     *
     * @param token payment token returned by SEP.
     * @return fully qualified redirect URI for the client.
     */
    public URI buildRedirectUrl(String token) {
        return URI.create(paymentPage.toString() + "?token=" + token);
    }
}
