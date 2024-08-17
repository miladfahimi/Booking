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
import java.util.stream.Collectors;

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
        ReservationDTO reservation = fetchReservation(reservationId);
        ProviderDTO provider = fetchProvider(reservation.getProviderId());
        ServiceDTO service = fetchService(reservation.getServiceId());
        UserProfileDTO userProfile = fetchUserProfile(reservation.getUserId());
        List<FeedbackDTO> providerFeedbacks = fetchProviderFeedbacks(reservation.getProviderId());
        List<FeedbackDTO> serviceFeedbacks = fetchServiceFeedbacks(reservation.getServiceId());

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
     * Fetches and aggregates full details for all reservations.
     *
     * @return a list of AggregatedReservationDTO objects with full details
     */
    public List<AggregatedReservationDTO> getAllAggregatedReservations() {
        List<ReservationDTO> reservations = getAllReservations();
        return reservations.stream()
                .map(reservation -> getAggregatedReservation(reservation.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Fetches a list of reservations.
     *
     * @return a list of ReservationDTO objects
     */
    public List<ReservationDTO> getAllReservations() {
        try {
            logger.info("Fetching all reservations");
            return reservationServiceClient.getAllReservations();
        } catch (FeignException e) {
            logger.error("Error occurred while fetching the reservations list: {}", e.getMessage());
            throw new ExternalServiceException("An error occurred while fetching the reservations list.", e);
        }
    }

    /**
     * Fetches a reservation by ID.
     *
     * @param reservationId the reservation ID
     * @return the ReservationDTO
     */
    private ReservationDTO fetchReservation(UUID reservationId) {
        try {
            logger.info("Fetching reservation with ID: {}", reservationId);
            return reservationServiceClient.getReservationById(reservationId);
        } catch (FeignException.NotFound e) {
            logger.error("Reservation with ID {} not found.", reservationId);
            throw new ReservationNotFoundException("Reservation with ID " + reservationId + " not found.", e);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching reservation details for ID {}: {}", reservationId, e.getMessage());
            throw new ExternalServiceException("An error occurred while fetching reservation details.", e);
        }
    }

    /**
     * Fetches a provider by ID.
     *
     * @param providerId the provider ID
     * @return the ProviderDTO
     */
    private ProviderDTO fetchProvider(UUID providerId) {
        try {
            logger.info("Fetching provider with ID: {}", providerId);
            return providerServiceClient.getProviderById(providerId);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching provider details for ID {}: {}", providerId, e.getMessage());
            return null;
        }
    }

    /**
     * Fetches a service by ID.
     *
     * @param serviceId the service ID
     * @return the ServiceDTO
     */
    private ServiceDTO fetchService(UUID serviceId) {
        try {
            logger.info("Fetching service with ID: {}", serviceId);
            return serviceServiceClient.getServiceById(serviceId);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching service details for ID {}: {}", serviceId, e.getMessage());
            return null;
        }
    }

    /**
     * Fetches a user profile by ID.
     *
     * @param userId the user ID
     * @return the UserProfileDTO
     */
    private UserProfileDTO fetchUserProfile(UUID userId) {
        try {
            logger.info("Fetching user profile with ID: {}", userId);
            return userProfileServiceClient.getUserProfileById(userId);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching user profile for ID {}: {}", userId, e.getMessage());
            return null;
        }
    }

    /**
     * Fetches feedbacks for a provider by ID.
     *
     * @param providerId the provider ID
     * @return a list of FeedbackDTO
     */
    private List<FeedbackDTO> fetchProviderFeedbacks(UUID providerId) {
        try {
            logger.info("Fetching feedbacks for provider ID: {}", providerId);
            return feedbackServiceClient.getFeedbacksByProviderId(providerId);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching feedbacks for provider ID {}: {}", providerId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Fetches feedbacks for a service by ID.
     *
     * @param serviceId the service ID
     * @return a list of FeedbackDTO
     */
    private List<FeedbackDTO> fetchServiceFeedbacks(UUID serviceId) {
        try {
            logger.info("Fetching feedbacks for service ID: {}", serviceId);
            return feedbackServiceClient.getFeedbacksByServiceId(serviceId);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching feedbacks for service ID {}: {}", serviceId, e.getMessage());
            return Collections.emptyList();
        }
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
