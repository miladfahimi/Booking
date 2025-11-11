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
public class SepPaymentProperties {

    private URI tokenEndpoint = URI.create("https://sep.shaparak.ir/onlinepg/onlinepg");
    private URI paymentPage = URI.create("https://sep.shaparak.ir/OnlinePG/SendToken");
    private URI verifyEndpoint = URI.create("https://sep.shaparak.ir/verifyTxnRandomSessionkey/ipg/VerifyTransaction");
    private URI reverseEndpoint = URI.create("https://sep.shaparak.ir/verifyTxnRandomSessionkey/ipg/ReverseTransaction");
    private String terminalId;
    private URI callbackUrl;
    private Integer defaultTokenExpiryMinutes = 20;
    private Boolean redirectWithGet = false;
    private Boolean mockEnabled = false;

    public URI tokenEndpoint() {
        return tokenEndpoint;
    }

    public void setTokenEndpoint(URI tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public URI paymentPage() {
        return paymentPage;
    }

    public void setPaymentPage(URI paymentPage) {
        this.paymentPage = paymentPage;
    }

    public URI verifyEndpoint() {
        return verifyEndpoint;
    }

    public void setVerifyEndpoint(URI verifyEndpoint) {
        this.verifyEndpoint = verifyEndpoint;
    }

    public URI reverseEndpoint() {
        return reverseEndpoint;
    }

    public void setReverseEndpoint(URI reverseEndpoint) {
        this.reverseEndpoint = reverseEndpoint;
    }

    public String terminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public URI callbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(URI callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer defaultTokenExpiryMinutes() {
        return defaultTokenExpiryMinutes;
    }

    public void setDefaultTokenExpiryMinutes(Integer defaultTokenExpiryMinutes) {
        this.defaultTokenExpiryMinutes = defaultTokenExpiryMinutes;
    }

    public Boolean redirectWithGet() {
        return redirectWithGet;
    }

    public void setRedirectWithGet(Boolean redirectWithGet) {
        this.redirectWithGet = redirectWithGet;
    }

    public Boolean mockEnabled() {
        return mockEnabled;
    }

    public void setMockEnabled(Boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
    }

    public URI buildRedirectUrl(String token) {
        return URI.create(paymentPage.toString() + "?token=" + token);
    }
}