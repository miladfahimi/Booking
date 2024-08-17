package com.tennistime.bff.application.service;

import com.tennistime.bff.application.dto.*;
import com.tennistime.bff.exceptions.ExternalServiceException;
import com.tennistime.bff.exceptions.ReservationNotFoundException;
import com.tennistime.bff.infrastructure.feign.FeedbackServiceClient;
import com.tennistime.bff.infrastructure.feign.ProviderServiceClient;
import com.tennistime.bff.infrastructure.feign.ReservationServiceClient;
import com.tennistime.bff.infrastructure.feign.ServiceServiceClient;
import com.tennistime.bff.infrastructure.feign.UserProfileServiceClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import feign.FeignException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationAggregationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationAggregationService.class);

    private final ReservationServiceClient reservationServiceClient;
    private final ProviderServiceClient providerServiceClient;
    private final ServiceServiceClient serviceServiceClient;
    private final FeedbackServiceClient feedbackServiceClient;
    private final UserProfileServiceClient userProfileServiceClient;

    /**
     * Aggregates reservation details, including provider, service, user profile, and feedbacks.
     *
     * @param reservationId the ID of the reservation to aggregate
     * @return an AggregatedReservationDTO containing all relevant information
     */
    public AggregatedReservationDTO getAggregatedReservation(UUID reservationId) {
        ReservationDTO reservation = null;
        ProviderDTO provider = null;
        ServiceDTO service = null;
        UserProfileDTO userProfile = null;
        List<FeedbackDTO> providerFeedbacks = Collections.emptyList();
        List<FeedbackDTO> serviceFeedbacks = Collections.emptyList();

        try {
            // Fetch Reservation
            logger.info("Fetching reservation with ID: {}", reservationId);
            reservation = reservationServiceClient.getReservationById(reservationId);
        } catch (FeignException.NotFound e) {
            logger.error("Reservation with ID {} not found.", reservationId);
            throw new ReservationNotFoundException("Reservation with ID " + reservationId + " not found.", e);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching reservation details for ID {}: {}", reservationId, e.getMessage());
            throw new ExternalServiceException("An error occurred while fetching reservation details.", e);
        }

        try {
            // Fetch Provider
            logger.info("Fetching provider with ID: {}", reservation.getProviderId());
            provider = providerServiceClient.getProviderById(reservation.getProviderId());
        } catch (FeignException e) {
            logger.error("Error occurred while fetching provider details for ID {}: {}", reservation.getProviderId(), e.getMessage());
        }

        try {
            // Fetch Service
            logger.info("Fetching service with ID: {}", reservation.getServiceId());
            service = serviceServiceClient.getServiceById(reservation.getServiceId());
        } catch (FeignException e) {
            logger.error("Error occurred while fetching service details for ID {}: {}", reservation.getServiceId(), e.getMessage());
        }

        try {
            // Fetch User Profile
            logger.info("Fetching user profile with ID: {}", reservation.getUserId());
            userProfile = userProfileServiceClient.getUserProfileById(reservation.getUserId());
        } catch (FeignException e) {
            logger.error("Error occurred while fetching user profile for ID {}: {}", reservation.getUserId(), e.getMessage());
        }

        try {
            // Fetch Feedbacks for the provider and service
            logger.info("Fetching feedbacks for provider ID: {}", reservation.getProviderId());
            providerFeedbacks = feedbackServiceClient.getFeedbacksByProviderId(reservation.getProviderId());
        } catch (FeignException e) {
            logger.error("Error occurred while fetching feedbacks for provider ID {}: {}", reservation.getProviderId(), e.getMessage());
        }

        try {
            logger.info("Fetching feedbacks for service ID: {}", reservation.getServiceId());
            serviceFeedbacks = feedbackServiceClient.getFeedbacksByServiceId(reservation.getServiceId());
        } catch (FeignException e) {
            logger.error("Error occurred while fetching feedbacks for service ID {}: {}", reservation.getServiceId(), e.getMessage());
        }

        return new AggregatedReservationDTO(
                reservation.getId(),
                reservation.getReservationDate(),
                reservation.getReservationDatePersian(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus(),
                userProfile,
                provider,
                service,
                serviceFeedbacks,
                providerFeedbacks
        );
    }

    /**
     * Method to test Feign client connectivity with the Reservation Service.
     *
     * @return A string indicating the status of the Feign client connection.
     */
    public String testReservationServiceFeignConnectivity() {
        try {
            logger.info("Testing Feign client connectivity with the Reservation Service");
            return reservationServiceClient.testReservationServiceFeignConnectivity();
        } catch (FeignException e) {
            logger.error("Error occurred while testing Feign client connectivity: {}", e.getMessage());
            throw new ExternalServiceException("An error occurred while testing Feign client connectivity.", e);
        }
    }
}
