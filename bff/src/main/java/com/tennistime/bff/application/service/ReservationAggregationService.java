package com.tennistime.bff.application.service;

import com.tennistime.bff.application.dto.*;
import com.tennistime.bff.exceptions.ExternalServiceException;
import com.tennistime.bff.exceptions.ReservationNotFoundException;
import com.tennistime.bff.infrastructure.feign.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import feign.FeignException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service to aggregate reservation details with associated entities such as providers, services, user profiles, and feedbacks.
 */
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
     * Aggregates the details of a reservation including associated provider, service, user profile, and feedbacks.
     *
     * @param reservationId the UUID of the reservation to aggregate
     * @return an AggregatedReservationDTO containing all relevant information
     * @throws ReservationNotFoundException if the reservation is not found
     * @throws ExternalServiceException     if there is an error communicating with external services
     */
    public AggregatedReservationDTO getAggregatedReservation(UUID reservationId) {
        logger.info("Aggregating details for reservation ID: {}", reservationId);

        // Fetch the core entities for the aggregation
        ReservationDTO reservation = fetchReservation(reservationId);
        ProviderDTO provider = fetchProvider(reservation.getProviderId());
        ServiceDTO service = fetchService(reservation.getServiceId());
        UserProfileDTO userProfile = fetchUserProfile(reservation.getUserId());

        // Fetch related feedbacks
        List<FeedbackDTO> providerFeedbacks = fetchProviderFeedbacks(reservation.getProviderId());
        List<FeedbackDTO> serviceFeedbacks = fetchServiceFeedbacks(reservation.getServiceId());

        // Aggregate the fetched data into a DTO
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
     * Retrieves all reservations and aggregates their details.
     *
     * @return a list of AggregatedReservationDTO objects with full details for each reservation
     * @throws ExternalServiceException if there is an error communicating with external services
     */
    public List<AggregatedReservationDTO> getAllAggregatedReservations() {
        logger.info("Fetching and aggregating all reservations");

        // Fetch all reservations
        List<ReservationDTO> reservations = getAllReservations();

        // Aggregate each reservation's details
        return reservations.stream()
                .map(reservation -> getAggregatedReservation(reservation.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Fetches a list of all providers along with their associated services.
     *
     * @return a list of ProviderDTO objects containing provider details and associated services
     * @throws ExternalServiceException if there is an error communicating with external services
     */
    public List<ProviderDTO> getProvidersWithServices() {
        logger.info("Fetching providers and their associated services");

        try {
            // Fetch all providers and associate their services
            return providerServiceClient.getAllProviders().stream()
                    .map(provider -> {
                        ProviderDTO providerDTO = new ProviderDTO();
                        providerDTO.setId(provider.getId());
                        providerDTO.setName(provider.getName());
                        providerDTO.setAddress(provider.getAddress());
                        providerDTO.setPhone(provider.getPhone());
                        providerDTO.setEmail(provider.getEmail());
                        providerDTO.setDescription(provider.getDescription());

                        List<ServiceDTO> services = serviceServiceClient.getServicesByProviderId(provider.getId());
                        providerDTO.setServices(services);

                        logger.debug("Processed provider with ID: {}", provider.getId());
                        return providerDTO;
                    })
                    .collect(Collectors.toList());
        } catch (FeignException e) {
            logger.error("Error occurred while fetching providers or services: {}", e.getMessage());
            throw new ExternalServiceException("An error occurred while fetching providers or services.", e);
        }
    }

    /**
     * Fetches all reservations.
     *
     * @return a list of ReservationDTO objects
     * @throws ExternalServiceException if there is an error communicating with external services
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
     * Fetches a reservation by its ID.
     *
     * @param reservationId the UUID of the reservation to fetch
     * @return the fetched ReservationDTO
     * @throws ReservationNotFoundException if the reservation is not found
     * @throws ExternalServiceException     if there is an error communicating with external services
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
     * Fetches a provider by its ID.
     *
     * @param providerId the UUID of the provider to fetch
     * @return the fetched ProviderDTO
     * @throws ExternalServiceException if there is an error communicating with external services
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
     * Fetches a service by its ID.
     *
     * @param serviceId the UUID of the service to fetch
     * @return the fetched ServiceDTO
     * @throws ExternalServiceException if there is an error communicating with external services
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
     * Fetches a user profile by its ID.
     *
     * @param userId the UUID of the user to fetch
     * @return the fetched UserProfileDTO
     * @throws ExternalServiceException if there is an error communicating with external services
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
     * Fetches feedbacks for a provider by its ID.
     *
     * @param providerId the UUID of the provider to fetch feedbacks for
     * @return a list of FeedbackDTO objects
     * @throws ExternalServiceException if there is an error communicating with external services
     */
    private List<FeedbackDTO> fetchProviderFeedbacks(UUID providerId) {
        try {
            logger.info("Fetching feedbacks for provider ID: {}", providerId);
            return feedbackServiceClient.getFeedbacksByProviderId(providerId);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching feedbacks for provider ID {}: {}", providerId, e.getMessage());
            return List.of();
        }
    }

    /**
     * Fetches feedbacks for a service by its ID.
     *
     * @param serviceId the UUID of the service to fetch feedbacks for
     * @return a list of FeedbackDTO objects
     * @throws ExternalServiceException if there is an error communicating with external services
     */
    private List<FeedbackDTO> fetchServiceFeedbacks(UUID serviceId) {
        try {
            logger.info("Fetching feedbacks for service ID: {}", serviceId);
            return feedbackServiceClient.getFeedbacksByServiceId(serviceId);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching feedbacks for service ID {}: {}", serviceId, e.getMessage());
            return List.of();
        }
    }

    /**
     * Method to test Feign client connectivity with the Reservation Service.
     *
     * @return A string indicating the status of the Feign client connection.
     * @throws ExternalServiceException if there is an error communicating with external services
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
