package com.tennistime.bff.application.controller;

import com.tennistime.bff.application.dto.payment.PaymentCallbackRequest;
import com.tennistime.bff.application.dto.payment.PaymentInitiationRequest;
import com.tennistime.bff.application.dto.payment.PaymentInitiationResponse;
import com.tennistime.bff.application.dto.payment.PaymentReverseResponse;
import com.tennistime.bff.application.dto.payment.PaymentVerificationResponse;
import com.tennistime.bff.application.service.PaymentApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller exposing payment operations for the frontend.
 */
@RestController
@RequestMapping("/portal/user/payments")
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;

    public PaymentController(PaymentApplicationService paymentApplicationService) {
        this.paymentApplicationService = paymentApplicationService;
    }

    /**
     * Initiates a payment session using the Reservation service backend.
     *
     * @param request payment initiation payload.
     * @return response containing redirect information.
     */
    @PostMapping("/create")
    public ResponseEntity<PaymentInitiationResponse> initiatePayment(@Valid @RequestBody PaymentInitiationRequest request) {
        PaymentInitiationResponse response = paymentApplicationService.initiatePayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles SEP callback payloads and forwards them for verification.
     *
     * @param request callback payload sent by SEP.
     * @return verification summary for the frontend.
     */
    @PostMapping("/callback")
    public ResponseEntity<PaymentVerificationResponse> handleCallback(@Valid @RequestBody PaymentCallbackRequest request) {
        PaymentVerificationResponse response = paymentApplicationService.handleCallback(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Requests reversal of a payment that was previously processed.
     *
     * @param paymentId identifier of the payment to reverse.
     * @return reversal outcome information.
     */
    @PostMapping("/{paymentId}/reverse")
    public ResponseEntity<PaymentReverseResponse> reversePayment(@PathVariable UUID paymentId) {
        PaymentReverseResponse response = paymentApplicationService.reversePayment(paymentId);
        return ResponseEntity.ok(response);
    }
}
