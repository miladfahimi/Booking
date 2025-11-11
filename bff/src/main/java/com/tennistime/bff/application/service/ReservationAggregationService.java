package com.tennistime.bff.application.service;

import com.tennistime.bff.application.dto.*;
import com.tennistime.bff.domain.model.types.ReservationStatus;
import com.tennistime.bff.exceptions.ExternalServiceException;
import com.tennistime.bff.exceptions.ReservationNotFoundException;
import com.tennistime.bff.exceptions.ServiceNotFoundException;
import com.tennistime.bff.infrastructure.feign.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import feign.FeignException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service responsible for aggregating reservation details and managing related entities.
 * This includes providers, services, user profiles, and feedbacks.
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
     */
    public AggregatedReservationDTO getAggregatedReservation(UUID reservationId) {
        logger.info("Aggregating details for reservation ID: {}", reservationId);

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
     * Retrieves and aggregates the details of all reservations.
     *
     * @return a list of AggregatedReservationDTO objects with full details for each reservation
     */
    public List<AggregatedReservationDTO> getAllAggregatedReservations() {
        logger.info("Fetching and aggregating all reservations");

        List<ReservationDTO> reservations = getAllReservations();
        return reservations.stream()
                .map(reservation -> getAggregatedReservation(reservation.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Fetches a list of all providers along with their associated services.
     *
     * @return a list of ProviderDTO objects containing provider details and associated services
     */
    public List<ProviderDTO> getProvidersWithServices() {
        logger.info("Fetching providers and their associated services");

        try {
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
     * Creates a reservation by delegating the request to the reservation service.
     *
     * @param reservationDTO the reservation details to persist
     * @return the created reservation
     */
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        try {
            logger.info("Creating reservation for user {} and service {}", reservationDTO.getUserId(), reservationDTO.getServiceId());
            return reservationServiceClient.createReservation(reservationDTO);
        } catch (FeignException e) {
            logger.error("Error occurred while creating reservation: {}", e.getMessage());
            throw new ExternalServiceException("An error occurred while creating the reservation.", e);
        }
    }

    /**
     * Creates multiple reservations in bulk by delegating to the reservation service.
     *
     * @param reservationDTOs the reservations to create
     * @return the list of created reservations
     */
    public List<ReservationDTO> createReservations(List<ReservationDTO> reservationDTOs) {
        try {
            int size = reservationDTOs != null ? reservationDTOs.size() : 0;
            logger.info("Creating {} reservations in bulk", size);
            return reservationServiceClient.createReservations(reservationDTOs);
        } catch (FeignException e) {
            logger.error("Error occurred while creating reservations in bulk: {}", e.getMessage());
            throw new ExternalServiceException("An error occurred while creating reservations in bulk.", e);
        }
    }
    /**
     * Fetches a reservation by its ID.
     *
     * @param reservationId the UUID of the reservation to fetch
     * @return the fetched ReservationDTO
     * @throws ReservationNotFoundException if the reservation is not found
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
     * @return the fetched ProviderDTO or null if not found
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
     * @return the fetched ServiceDTO or null if not found
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
     * @return the fetched UserProfileDTO or null if not found
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
     * @return a list of FeedbackDTO objects or an empty list if none found
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
     * @return a list of FeedbackDTO objects or an empty list if none found
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
     * Tests Feign client connectivity with the Reservation Service.
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

    /**
     * Calculates available slots for a service based on its start time, end time, and slot duration.
     *
     * @param serviceId the UUID of the service
     * @return a list of SlotDTO objects representing the available slots
     * @throws ServiceNotFoundException if the service is not found
     */
    public List<SlotDTO> calculateSlots(UUID serviceId) {
        logger.info("Calculating slots for service ID: {}", serviceId);

        ServiceDTO serviceDTO = serviceServiceClient.getServiceById(serviceId);
        if (serviceDTO == null) {
            throw new ServiceNotFoundException("Service not found for ID: " + serviceId);
        }

        return generateSlots(serviceDTO);
    }

    /**
     * Calculates and updates the availability slots for a service based on existing reservations.
     *
     * @param serviceId the UUID of the service
     * @param date      the date for which the slots should be checked
     * @return a ServiceDTO populated with slots information and slot count
     * @throws ServiceNotFoundException if the service is not found
     */
    public ServiceDTO calculateAndUpdateSlots(UUID serviceId, LocalDate date) {
        logger.info("Calculating and updating slots for service ID: {} on date: {}", serviceId, date);

        ServiceDTO serviceDTO = serviceServiceClient.getServiceById(serviceId);
        if (serviceDTO == null) {
            throw new ServiceNotFoundException("Service not found for ID: " + serviceId);
        }

        List<SlotDTO> slots = generateSlots(serviceDTO);
        List<ReservationDTO> reservations = fetchReservationsByServiceAndDate(serviceId, date);

        updateSlotsWithReservations(slots, serviceDTO, reservations);
        serviceDTO.setSlots(slots);
        serviceDTO.setSlotCount(slots.size());  // Set the slot count

        return serviceDTO;
    }

    /**
     * Retrieves all services filtered by type and populates their slots for the requested date.
     *
     * @param type the service type to filter by; if {@code null}, blank, or representing "all", no type filter is applied
     * @param date the target date for which the slot availability should be calculated
     * @return a list of {@link ServiceDTO} instances enriched with slot information
     */
    public List<ServiceDTO> getServiceSlotsByTypeAndDate(String type, LocalDate date) {
        logger.info("Fetching slots for services filtered by type: {} on date: {}", type, date);

        String normalizedType = normalizeTypeFilter(type);

        List<ServiceDTO> services;
        try {
            services = serviceServiceClient.getAllServices();
        } catch (FeignException e) {
            logger.error("Error occurred while fetching services for slot aggregation: {}", e.getMessage());
            throw new ExternalServiceException("An error occurred while fetching services.", e);
        }

        return services.stream()
                .filter(service -> matchesType(normalizedType, service.getType()))
                .map(service -> enrichServiceWithSlots(service, date))
                .collect(Collectors.toList());
    }

    /**
     * Generates a list of slots with detailed information for a service based on its start time, end time, and slot duration.
     *
     * @param serviceDTO the service details
     * @return a list of SlotDTO objects
     */
    private List<SlotDTO> generateSlots(ServiceDTO serviceDTO) {
        LocalTime startTime = serviceDTO.getStartTime();
        LocalTime endTime = serviceDTO.getEndTime();
        int slotDuration = serviceDTO.getSlotDuration();
        int slotGapDuration = serviceDTO.getSlotGapDuration() != null ? serviceDTO.getSlotGapDuration() : 0;
        int effectiveSlotUsage = Math.max(slotDuration - slotGapDuration, 0);
        BigDecimal price = serviceDTO.getPrice();

        List<SlotDTO> slots = new ArrayList<>();
        LocalTime currentTime = startTime;
        int slotIndex = 1;

        while (currentTime.isBefore(endTime)) {
            SlotDTO slot = new SlotDTO();
            slot.setSlotId("slot-" + slotIndex);
            slot.setTime(currentTime.toString());
            LocalTime slotEndTime = effectiveSlotUsage == 0 ? currentTime : currentTime.plusMinutes(effectiveSlotUsage);
            slot.setEndTime(slotEndTime.toString());
            slot.setStatus(ReservationStatus.AVAILABLE);
            slot.setPrice(price);
            slot.setCapacity(serviceDTO.getMaxCapacity());
            slot.setReservedBy(null);

            slots.add(slot);
            currentTime = currentTime.plusMinutes(slotDuration);
            slotIndex++;
        }
        return slots;
    }

    /**
     * Determines whether a service matches the provided type filter.
     *
     * @param normalizedType the normalized type filter (may be {@code null})
     * @param serviceType    the type associated with the service
     * @return {@code true} if the service should be included; {@code false} otherwise
     */
    private boolean matchesType(String normalizedType, String serviceType) {
        if (normalizedType == null) {
            return true;
        }
        return serviceType != null && serviceType.equalsIgnoreCase(normalizedType);
    }

    /**
     * Normalizes the provided type filter value.
     *
     * @param type the raw type value received from the API
     * @return a normalized type string or {@code null} if the filter should be ignored
     */
    private String normalizeTypeFilter(String type) {
        if (type == null) {
            return null;
        }

        String trimmedType = type.trim();
        if (trimmedType.isEmpty()) {
            return null;
        }

        if ("all".equalsIgnoreCase(trimmedType) || "null".equalsIgnoreCase(trimmedType) || "undefined".equalsIgnoreCase(trimmedType)) {
            return null;
        }

        return trimmedType;
    }

    /**
     * Populates the slot information for the provided service and date.
     *
     * @param serviceDTO the service whose slots should be enriched
     * @param date       the date used to calculate availability
     * @return a new {@link ServiceDTO} instance containing slot information
     */
    private ServiceDTO enrichServiceWithSlots(ServiceDTO serviceDTO, LocalDate date) {
        ServiceDTO enrichedService = cloneService(serviceDTO);

        List<SlotDTO> slots = generateSlots(enrichedService);
        List<ReservationDTO> reservations = fetchReservationsByServiceAndDate(enrichedService.getId(), date);

        updateSlotsWithReservations(slots, enrichedService, reservations);
        enrichedService.setSlots(slots);
        enrichedService.setSlotCount(slots.size());

        return enrichedService;
    }

    /**
     * Creates a shallow copy of the provided service to avoid mutating shared instances.
     *
     * @param serviceDTO the service to copy
     * @return a new {@link ServiceDTO} instance with duplicated state
     */
    private ServiceDTO cloneService(ServiceDTO serviceDTO) {
        ServiceDTO clone = new ServiceDTO();
        clone.setId(serviceDTO.getId());
        clone.setName(serviceDTO.getName());
        clone.setType(serviceDTO.getType());
        clone.setAvailability(serviceDTO.isAvailability());
        clone.setProviderId(serviceDTO.getProviderId());
        clone.setStartTime(serviceDTO.getStartTime());
        clone.setEndTime(serviceDTO.getEndTime());
        clone.setSlotDuration(serviceDTO.getSlotDuration());
        clone.setSlotGapDuration(serviceDTO.getSlotGapDuration());
        clone.setDescription(serviceDTO.getDescription());
        clone.setTags(serviceDTO.getTags() != null ? new ArrayList<>(serviceDTO.getTags()) : null);
        clone.setPrice(serviceDTO.getPrice());
        clone.setMaxCapacity(serviceDTO.getMaxCapacity());
        clone.setSlots(null);
        clone.setSlotCount(serviceDTO.getSlotCount());
        return clone;
    }

    /**
     * Fetches reservations for a given service and date using the updated filtering endpoint.
     *
     * @param serviceId the UUID of the service
     * @param date      the date for which reservations should be fetched
     * @return a list of ReservationDTO objects
     */
    private List<ReservationDTO> fetchReservationsByServiceAndDate(UUID serviceId, LocalDate date) {
        try {
            logger.info("Fetching reservations for service ID: {} on date: {}", serviceId, date);
            return reservationServiceClient.getReservationsByFilters(null, serviceId, null, null, date, null, null, null);
        } catch (FeignException e) {
            logger.error("Error occurred while fetching reservations for service ID {} on date {}: {}", serviceId, date, e.getMessage());
            throw new ExternalServiceException("An error occurred while fetching reservations.", e);
        }
    }

    /**
     * Updates the availability slots based on existing reservations.
     *
     * @param slots        the list of SlotDTO objects to be updated
     * @param serviceDTO   the service details
     * @param reservations the list of reservations to be used for updating slots
     */
    private void updateSlotsWithReservations(List<SlotDTO> slots, ServiceDTO serviceDTO, List<ReservationDTO> reservations) {
        LocalTime startTime = serviceDTO.getStartTime();
        int slotDuration = serviceDTO.getSlotDuration();

        for (ReservationDTO reservation : reservations) {
            LocalTime reservationTime = reservation.getStartTime();
            int slotIndex = (int) (java.time.Duration.between(startTime, reservationTime).toMinutes() / slotDuration);

            if (slotIndex >= 0 && slotIndex < slots.size()) {
                SlotDTO slot = slots.get(slotIndex);

                if (slot.getStatus() != null && slot.getStatus() != ReservationStatus.AVAILABLE) {
                    continue;
                }

                ReservationStatus reservationStatus = Optional.ofNullable(reservation.getStatus())
                        .orElse(ReservationStatus.PENDING);

                if (reservationStatus == ReservationStatus.CANCELED || reservationStatus == ReservationStatus.EXPIRED) {
                    continue;
                }

                slot.setStatus(reservationStatus);

                if (reservationStatus == ReservationStatus.CONFIRMED || reservationStatus == ReservationStatus.PENDING) {
                    slot.setReservedBy(Optional.ofNullable(reservation.getUserId())
                            .map(UUID::toString)
                            .orElse(null));
                    slot.setPrice(slot.getPrice().multiply(new BigDecimal("1.2"))); // Example: Increase price for reserved slots (premium pricing)
                } else {
                    slot.setReservedBy(null);
                }
            }
        }
    }
}
