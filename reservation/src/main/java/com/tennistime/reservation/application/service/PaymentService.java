package com.tennistime.reservation.application.service;

import com.tennistime.reservation.application.dto.payment.PaymentCallbackRequest;
import com.tennistime.reservation.application.dto.payment.PaymentInitiationRequest;
import com.tennistime.reservation.application.dto.payment.PaymentInitiationResponse;
import com.tennistime.reservation.application.dto.payment.PaymentReverseResponse;
import com.tennistime.reservation.application.dto.payment.PaymentVerificationResponse;
import com.tennistime.reservation.domain.payment.Payment;
import com.tennistime.reservation.domain.payment.PaymentRepository;
import com.tennistime.reservation.domain.payment.exception.PaymentGatewayException;
import com.tennistime.reservation.domain.payment.exception.PaymentNotFoundException;
import com.tennistime.reservation.infrastructure.payment.sep.SepPaymentClient;
import com.tennistime.reservation.infrastructure.payment.sep.SepPaymentProperties;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepReverseRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepReverseResponse;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepTokenRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepTokenResponse;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepVerifyRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepVerifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Objects;

/**
 * Application service coordinating the payment flow between the domain and the SEP gateway.
 */
@Service
@Transactional
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final SepPaymentClient sepPaymentClient;
    private final SepPaymentProperties paymentProperties;

    public PaymentService(PaymentRepository paymentRepository,
                          SepPaymentClient sepPaymentClient,
                          SepPaymentProperties paymentProperties) {
        this.paymentRepository = paymentRepository;
        this.sepPaymentClient = sepPaymentClient;
        this.paymentProperties = paymentProperties;
    }

    /**
     * Initiates a new payment by requesting a token from SEP and storing the aggregate state.
     *
     * @param request payment request submitted by the client.
     * @return response containing the token and redirect URL.
     */
    public PaymentInitiationResponse initiatePayment(PaymentInitiationRequest request) {
        BigDecimal normalizedAmount = sanitizeAmount(request.getAmount());
        Payment payment = Payment.create(request.getReservationId(), normalizedAmount);
        paymentRepository.save(payment);

        if (paymentProperties.terminalId() == null || paymentProperties.callbackUrl() == null) {
            throw new PaymentGatewayException("SEP terminal or callback URL is not configured");
        }

        if (Boolean.TRUE.equals(paymentProperties.mockEnabled())) {
            String token = "MOCK-" + payment.getReferenceNumber();
            payment.registerToken(token);
            paymentRepository.save(payment);

            PaymentInitiationResponse response = new PaymentInitiationResponse();
            response.setPaymentId(payment.getId());
            response.setToken(token);
            response.setReferenceNumber(payment.getReferenceNumber());
            response.setRedirectUrl(paymentProperties.buildRedirectUrl(token));
            return response;
        }


        SepTokenRequest tokenRequest = new SepTokenRequest(
                "token",
                paymentProperties.terminalId(),
                normalizedAmount.longValue(),
                payment.getReferenceNumber(),
                paymentProperties.callbackUrl().toString(),
                request.getCustomerCellNumber(),
                resolveExpiry(request),
                request.getHashedCardNumber(),
                resolveGetMethod(request)
        );

        SepTokenResponse tokenResponse = sepPaymentClient.requestToken(tokenRequest);
        if (tokenResponse == null || !tokenResponse.isSuccessful()) {
            LOGGER.warn("Failed to obtain payment token for ResNum {} - status {}", payment.getReferenceNumber(),
                    tokenResponse != null ? tokenResponse.status() : null);
            throw new PaymentGatewayException("Unable to obtain payment token from SEP");
        }

        payment.registerToken(tokenResponse.token());
        paymentRepository.save(payment);

        PaymentInitiationResponse response = new PaymentInitiationResponse();
        response.setPaymentId(payment.getId());
        response.setToken(tokenResponse.token());
        response.setReferenceNumber(payment.getReferenceNumber());
        URI redirectUrl = paymentProperties.buildRedirectUrl(tokenResponse.token());
        response.setRedirectUrl(redirectUrl);
        return response;
    }

    /**
     * Handles the callback notification sent by SEP and triggers the verification call.
     *
     * @param request payload received from SEP.
     * @return response summarising the verification result.
     */
    public PaymentVerificationResponse processCallback(PaymentCallbackRequest request) {
        Payment payment = paymentRepository.findByReferenceNumber(request.getResNum())
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for reference " + request.getResNum()));

        payment.registerCallback(request.getState(), request.getStatus(), request.getRefNum(), request.getRrn(),
                request.getTraceNo());

        PaymentVerificationResponse response = new PaymentVerificationResponse();
        response.setPaymentId(payment.getId());

        if (Boolean.TRUE.equals(paymentProperties.mockEnabled())) {
            payment.markVerified();
            response.setSuccess(true);
            response.setMessage("Payment verified in mock mode");
            response.setBankReferenceNumber(
                    request.getRefNum() != null && !request.getRefNum().isBlank() ? request.getRefNum() : payment.getReferenceNumber());
            response.setRetrievalReferenceNumber(request.getRrn());
            response.setVerifiedAt(payment.getVerifiedAt());
            paymentRepository.save(payment);
            return response;
        }


        if (request.getRefNum() == null || request.getRefNum().isBlank()) {
            payment.markFailed();
            response.setSuccess(false);
            response.setMessage("Verification skipped because RefNum was not provided");
            paymentRepository.save(payment);
            return response;
        }

        SepVerifyResponse verifyResponse = sepPaymentClient.verifyTransaction(
                new SepVerifyRequest(request.getRefNum(), paymentProperties.terminalId()));

        if (verifyResponse != null && verifyResponse.success()) {
            payment.markVerified();
            response.setSuccess(true);
            response.setMessage("Payment verified successfully");
            response.setBankReferenceNumber(request.getRefNum());
            response.setRetrievalReferenceNumber(request.getRrn());
            response.setVerifiedAt(payment.getVerifiedAt());
        } else {
            payment.markFailed();
            response.setSuccess(false);
            String errorDescription = verifyResponse != null ? verifyResponse.resultDescription() : "No response";
            response.setMessage("Verification failed: " + errorDescription);
        }

        paymentRepository.save(payment);
        return response;
    }

    /**
     * Initiates a reverse transaction on SEP after a payment has been verified.
     *
     * @param paymentId identifier of the payment to reverse.
     * @return response containing the reverse outcome.
     */
    public PaymentReverseResponse reversePayment(java.util.UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for id " + paymentId));

        if (!payment.canBeReversed()) {
            throw new PaymentGatewayException("Payment cannot be reversed because it is not verified yet");
        }

        if (Boolean.TRUE.equals(paymentProperties.mockEnabled())) {
            payment.markReversed();
            PaymentReverseResponse response = new PaymentReverseResponse();
            response.setPaymentId(payment.getId());
            response.setReversed(true);
            response.setMessage("Reverse transaction completed in mock mode");
            response.setReversedAt(payment.getReversedAt());
            paymentRepository.save(payment);
            return response;
        }


        SepReverseResponse reverseResponse = sepPaymentClient.reverseTransaction(
                new SepReverseRequest(payment.getBankReferenceNumber(), paymentProperties.terminalId()));

        PaymentReverseResponse response = new PaymentReverseResponse();
        response.setPaymentId(payment.getId());
        if (reverseResponse != null && reverseResponse.success()) {
            payment.markReversed();
            response.setReversed(true);
            response.setMessage("Reverse transaction succeeded");
            response.setReversedAt(payment.getReversedAt());
        } else {
            response.setReversed(false);
            String resultDescription = reverseResponse != null ? reverseResponse.resultDescription() : "No response";
            response.setMessage("Reverse transaction failed: " + resultDescription);
        }

        paymentRepository.save(payment);
        return response;
    }

    /**
     * Normalises the supplied amount so the request payload complies with SEP requirements.
     *
     * @param amount amount requested by the client.
     * @return rounded amount using rial precision.
     */
    private BigDecimal sanitizeAmount(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        return amount.setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Determines the token expiry in minutes falling back to the configured default.
     *
     * @param request client request containing an optional override.
     * @return token expiry to be sent to SEP.
     */
    private Integer resolveExpiry(PaymentInitiationRequest request) {
        return Objects.requireNonNullElse(request.getTokenExpiryInMinutes(), paymentProperties.defaultTokenExpiryMinutes());
    }

    /**
     * Determines whether a GET redirect should be used when building the payment form.
     *
     * @param request client request containing an optional hint.
     * @return {@code true} if the GET flow should be used.
     */
    private Boolean resolveGetMethod(PaymentInitiationRequest request) {
        return Objects.requireNonNullElse(request.getUseGetMethod(), paymentProperties.redirectWithGet());
    }
}
