package com.tennistime.reservation.infrastructure.payment.sep;

import com.tennistime.reservation.infrastructure.payment.sep.dto.SepReverseRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepReverseResponse;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepTokenRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepTokenResponse;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepVerifyRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepVerifyResponse;

/**
 * Contract used to interact with the SEP acquiring gateway. This client hides the HTTP
 * interaction while exposing simple Java objects to the application layer.
 */
public interface SepPaymentClient {

    /**
     * Requests a payment token from SEP so the customer can be redirected to the bank page.
     *
     * @param request token request payload.
     * @return response containing the token or an error description.
     */
    SepTokenResponse requestToken(SepTokenRequest request);

    /**
     * Verifies the transaction by calling SEP's verification endpoint.
     *
     * @param request verification request containing the reference number.
     * @return verification response returned by SEP.
     */
    SepVerifyResponse verifyTransaction(SepVerifyRequest request);

    /**
     * Performs a reverse transaction call on SEP when a charge needs to be cancelled.
     *
     * @param request reverse request referencing the original transaction.
     * @return result of the reverse attempt.
     */
    SepReverseResponse reverseTransaction(SepReverseRequest request);
}
