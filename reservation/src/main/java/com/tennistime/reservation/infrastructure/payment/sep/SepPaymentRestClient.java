package com.tennistime.reservation.infrastructure.payment.sep;

import com.tennistime.reservation.infrastructure.payment.sep.dto.SepReverseRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepReverseResponse;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepTokenRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepTokenResponse;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepVerifyRequest;
import com.tennistime.reservation.infrastructure.payment.sep.dto.SepVerifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Default {@link SepPaymentClient} implementation backed by Spring's {@link RestTemplate}.
 */
@Component
public class SepPaymentRestClient implements SepPaymentClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SepPaymentRestClient.class);

    private final RestTemplate restTemplate;
    private final SepPaymentProperties properties;

    public SepPaymentRestClient(RestTemplate restTemplate, SepPaymentProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public SepTokenResponse requestToken(SepTokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SepTokenRequest> entity = new HttpEntity<>(request, headers);
        LOGGER.debug("Requesting SEP token for ResNum {}", request.referenceNumber());
        return restTemplate.postForObject(properties.tokenEndpoint(), entity, SepTokenResponse.class);
    }

    @Override
    public SepVerifyResponse verifyTransaction(SepVerifyRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SepVerifyRequest> entity = new HttpEntity<>(request, headers);
        LOGGER.debug("Verifying SEP transaction for RefNum {}", request.referenceNumber());
        return restTemplate.postForObject(properties.verifyEndpoint(), entity, SepVerifyResponse.class);
    }

    @Override
    public SepReverseResponse reverseTransaction(SepReverseRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SepReverseRequest> entity = new HttpEntity<>(request, headers);
        LOGGER.debug("Reversing SEP transaction for RefNum {}", request.referenceNumber());
        return restTemplate.postForObject(properties.reverseEndpoint(), entity, SepReverseResponse.class);
    }
}
