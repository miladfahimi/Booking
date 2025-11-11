package com.tennistime.reservation.application.controller;

import com.tennistime.reservation.application.dto.payment.PaymentCallbackRequest;
import com.tennistime.reservation.application.dto.payment.PaymentInitiationRequest;
import com.tennistime.reservation.application.dto.payment.PaymentInitiationResponse;
import com.tennistime.reservation.application.dto.payment.PaymentReverseResponse;
import com.tennistime.reservation.application.dto.payment.PaymentVerificationResponse;
import com.tennistime.reservation.application.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller exposing the payment flow endpoints for the reservation service.
 */
@RestController
@RequestMapping("/payments")
@Tag(name = "Payments", description = "Endpoints for initiating and verifying payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Initiates a payment by requesting a token from SEP.
     *
     * @param request payment initiation payload.
     * @return response containing the token and redirect URL.
     */
    @PostMapping("/create")
    @Operation(summary = "Create payment", description = "Requests a token from SEP and returns the redirect URL")
    public ResponseEntity<PaymentInitiationResponse> initiatePayment(@Valid @RequestBody PaymentInitiationRequest request) {
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    /**
     * Endpoint invoked by SEP after the customer leaves the payment page.
     *
     * @param request callback payload provided by SEP.
     * @return verification summary for the client UI.
     */
    @PostMapping("/callback")
    @Operation(summary = "Handle callback", description = "Processes the SEP callback and verifies the payment")
    public ResponseEntity<PaymentVerificationResponse> handleCallback(@Valid @RequestBody PaymentCallbackRequest request) {
        return ResponseEntity.ok(paymentService.processCallback(request));
    }

    /**
     * Allows operators to reverse a payment after it was successfully verified.
     *
     * @param paymentId identifier of the payment to reverse.
     * @return reverse result payload.
     */
    @PostMapping("/{paymentId}/reverse")
    @Operation(summary = "Reverse payment", description = "Issues a reverse request for a verified payment")
    public ResponseEntity<PaymentReverseResponse> reversePayment(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.reversePayment(paymentId));
    }
}
