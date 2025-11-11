package com.tennistime.bff.application.service;

import com.tennistime.bff.application.dto.payment.PaymentCallbackRequest;
import com.tennistime.bff.application.dto.payment.PaymentInitiationRequest;
import com.tennistime.bff.application.dto.payment.PaymentInitiationResponse;
import com.tennistime.bff.application.dto.payment.PaymentReverseResponse;
import com.tennistime.bff.application.dto.payment.PaymentVerificationResponse;
import com.tennistime.bff.exceptions.ExternalServiceException;
import com.tennistime.bff.infrastructure.feign.PaymentServiceClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Application service responsible for orchestrating payment operations through the Reservation service.
 */
@Service
public class PaymentApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentApplicationService.class);

    private final PaymentServiceClient paymentServiceClient;

    public PaymentApplicationService(PaymentServiceClient paymentServiceClient) {
        this.paymentServiceClient = paymentServiceClient;
    }

    /**
     * Initiates a payment by delegating the request to the Reservation service.
     *
     * @param request payload containing reservation and payment metadata.
     * @return payment initiation response with redirect details.
     */
    public PaymentInitiationResponse initiatePayment(PaymentInitiationRequest request) {
        logger.info("Initiating payment for reservation {}", request.getReservationId());
        try {
            return paymentServiceClient.initiatePayment(request);
        } catch (FeignException e) {
            logger.error("Failed to initiate payment for reservation {}: {}", request.getReservationId(), e.getMessage());
            throw new ExternalServiceException("An error occurred while initiating the payment.", e);
        }
    }

    /**
     * Handles the payment callback payload and forwards it to the Reservation service for verification.
     *
     * @param request callback payload received from SEP.
     * @return verification result describing the payment status.
     */
    public PaymentVerificationResponse handleCallback(PaymentCallbackRequest request) {
        logger.info("Processing payment callback for ResNum {}", request.getResNum());
        try {
            return paymentServiceClient.handleCallback(request);
        } catch (FeignException e) {
            logger.error("Failed to process payment callback for ResNum {}: {}", request.getResNum(), e.getMessage());
            throw new ExternalServiceException("An error occurred while processing the payment callback.", e);
        }
    }

    /**
     * Requests a payment reversal from the Reservation service.
     *
     * @param paymentId identifier of the payment to reverse.
     * @return reversal response returned by the Reservation service.
     */
    public PaymentReverseResponse reversePayment(UUID paymentId) {
        logger.info("Reversing payment {}", paymentId);
        try {
            return paymentServiceClient.reversePayment(paymentId);
        } catch (FeignException e) {
            logger.error("Failed to reverse payment {}: {}", paymentId, e.getMessage());
            throw new ExternalServiceException("An error occurred while reversing the payment.", e);
        }
    }
}
