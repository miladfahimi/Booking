package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.payment.PaymentCallbackRequest;
import com.tennistime.bff.application.dto.payment.PaymentInitiationRequest;
import com.tennistime.bff.application.dto.payment.PaymentInitiationResponse;
import com.tennistime.bff.application.dto.payment.PaymentReverseResponse;
import com.tennistime.bff.application.dto.payment.PaymentVerificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Feign client for interacting with the Reservation payment endpoints.
 */
@FeignClient(name = "reservation-payment-service", url = "http://reservation:8085/api/v1")
public interface PaymentServiceClient {

    @PostMapping("/payments/create")
    PaymentInitiationResponse initiatePayment(@RequestBody PaymentInitiationRequest request);

    @PostMapping("/payments/callback")
    PaymentVerificationResponse handleCallback(@RequestBody PaymentCallbackRequest request);

    @PostMapping("/payments/{paymentId}/reverse")
    PaymentReverseResponse reversePayment(@PathVariable("paymentId") UUID paymentId);
}
