package com.tennistime.reservation.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.reservation.application.dto.ReservationDTO;
import com.tennistime.reservation.application.mapper.ReservationMapper;
import com.tennistime.reservation.application.notification.SlotStatusNotifier;
import com.tennistime.reservation.domain.model.Reservation;
import com.tennistime.reservation.domain.model.types.ReservationStatus;
import com.tennistime.reservation.domain.notification.SlotStatusNotification;
import com.tennistime.reservation.domain.repository.ReservationBasketItemRepository;
import com.tennistime.reservation.domain.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing reservations.
 * Handles the creation, update, retrieval, and deletion of reservation records.
 */
@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UserBookingHistoryUpdater userBookingHistoryUpdater;
    private final SlotStatusNotifier slotStatusNotifier;
    private final ReservationBasketItemRepository basketItemRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationMapper reservationMapper,
                              UserBookingHistoryUpdater userBookingHistoryUpdater,
                              SlotStatusNotifier slotStatusNotifier,
                              ReservationBasketItemRepository basketItemRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.userBookingHistoryUpdater = userBookingHistoryUpdater;
        this.slotStatusNotifier = slotStatusNotifier;
        this.basketItemRepository = basketItemRepository;
    }

    /**
     * Retrieves all reservation records.
     *
     * @return List of ReservationDTO representing all reservations.
     */
    public List<ReservationDTO> findAll() {
        logger.info("Fetching all reservations.");
        return reservationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a reservation record by its UUID.
     *
     * @param id UUID of the reservation to retrieve.
     * @return ReservationDTO representing the reservation, or null if not found.
     */
    public ReservationDTO findById(UUID id) {
        logger.info("Fetching reservation by ID: {}", id);
        return reservationRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public ReservationDTO save(ReservationDTO reservationDTO) {
        logger.info("Creating new reservation.");
        Reservation reservation = convertToEntity(reservationDTO);
        Reservation savedReservation = reservationRepository.save(reservation);

        userBookingHistoryUpdater.updateUserBookingHistory(savedReservation);
        notifyStatusChange(savedReservation);

        return convertToDTO(savedReservation);
    }

    /**
     * Creates multiple reservations in a single request, forcing each to start in {@link ReservationStatus#PENDING} state.
     *
     * @param reservationDTOs reservations to persist.
     * @return list containing the persisted reservations represented as DTOs.
     */
    public List<ReservationDTO> saveAll(List<ReservationDTO> reservationDTOs) {
        int requestedReservations = reservationDTOs != null ? reservationDTOs.size() : 0;
        logger.info("Creating {} reservations in bulk mode.", requestedReservations);
        if (reservationDTOs == null || reservationDTOs.isEmpty()) {
            return List.of();
        }
        List<Reservation> savedReservations = reservationDTOs.stream()
                .map(this::convertToEntity)
                .map(reservationRepository::save)
                .peek(userBookingHistoryUpdater::updateUserBookingHistory)
                .peek(this::notifyStatusChange)
                .collect(Collectors.toList());

        savedReservations.forEach(reservation ->
                removeBasketEntry(reservation.getUserId(), reservation.getSlotId()));

        return savedReservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing reservation record.
     *
     * @param id             UUID of the reservation to update.
     * @param reservationDTO DTO containing the updated details of the reservation.
     * @return The updated ReservationDTO, or null if the reservation was not found.
     */
    public ReservationDTO updateReservation(UUID id, ReservationDTO reservationDTO) {
        logger.info("Updating reservation with ID: {}", id);
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    updateReservationDetails(existingReservation, reservationDTO);
                    Reservation updatedReservation = reservationRepository.save(existingReservation);
                    userBookingHistoryUpdater.updateUserBookingHistory(updatedReservation);
                    notifyStatusChange(updatedReservation);
                    return convertToDTO(updatedReservation);
                })
                .orElse(null);
    }

    /**
     * Updates the status of an existing reservation.
     *
     * @param id     UUID of the reservation to update.
     * @param status New status for the reservation.
     * @return The updated ReservationDTO, or null if the reservation was not found.
     */
    public ReservationDTO updateReservationStatus(UUID id, ReservationStatus status) {
        logger.info("Updating status of reservation with ID: {}", id);
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    existingReservation.setStatus(status);
                    Reservation updatedReservation = reservationRepository.save(existingReservation);
                    userBookingHistoryUpdater.updateUserBookingHistory(updatedReservation);
                    notifyStatusChange(updatedReservation);
                    return convertToDTO(updatedReservation);
                })
                .orElse(null);
    }

    /**
     * Deletes a reservation record by its UUID.
     *
     * @param id UUID of the reservation to delete.
     */
    public void deleteById(UUID id) {
        logger.info("Deleting reservation with ID: {}", id);
        reservationRepository.deleteById(id);
    }

    /**
     * Retrieves all reservations made by a specific user.
     *
     * @param userId UUID of the user whose reservations to retrieve.
     * @return List of ReservationDTO representing the user's reservations.
     */
    public List<ReservationDTO> findReservationsByUserId(UUID userId) {
        logger.info("Fetching reservations for user ID: {}", userId);
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all reservations for a specific service.
     *
     * @param serviceId ID of the service whose reservations to retrieve.
     * @return List of ReservationDTO representing the service's reservations.
     */
    public List<ReservationDTO> findReservationsByServiceId(UUID serviceId) {
        logger.info("Fetching reservations for service ID: {}", serviceId);
        return reservationRepository.findByServiceId(serviceId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all reservations made on a specific Persian date.
     *
     * @param persianDateStr Persian date string to filter reservations by.
     * @return List of ReservationDTO representing the reservations on the specified date.
     */
    public List<ReservationDTO> findReservationsByPersianDate(String persianDateStr) {
        logger.info("Fetching reservations for Persian date: {}", persianDateStr);
        LocalDate gregorianDate = PersianDate.parse(persianDateStr).toGregorian();
        return reservationRepository.findAll()
                .stream()
                .filter(reservation -> gregorianDate.equals(reservation.getReservationDate()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve reservations filtered by various optional parameters.
     *
     * @param userId          UUID of the user (optional).
     * @param serviceId       UUID of the service (optional).
     * @param providerId      UUID of the provider (optional).
     * @param status          Status of the reservation (optional).
     * @param reservationDate Reservation date in Gregorian calendar (optional).
     * @param persianDateStr  Reservation date in Persian calendar (optional).
     * @param startTime       Start time of the reservation (optional).
     * @param endTime         End time of the reservation (optional).
     * @param referenceNumber Reference number of the reservation (optional).
     * @return List of ReservationDTOs matching the filters.
     */
    public List<ReservationDTO> findReservations(UUID userId, UUID serviceId, UUID providerId, ReservationStatus status,
                                                 LocalDate reservationDate, String persianDateStr, LocalTime startTime,
                                                 LocalTime endTime, String referenceNumber) {
        logger.info("Filtering reservations with multiple criteria.");
        LocalDate persianToGregorianDate = persianDateStr != null ? PersianDate.parse(persianDateStr).toGregorian() : null;

        return reservationRepository.findAll().stream()
                .filter(reservation -> matchesFilter(reservation, userId, serviceId, providerId, status,
                        reservationDate, persianToGregorianDate, startTime, endTime, referenceNumber))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a reservation matches the provided filter criteria.
     *
     * @param reservation            The reservation to check.
     * @param userId                 UUID of the user.
     * @param serviceId              UUID of the service.
     * @param providerId             UUID of the provider.
     * @param status                 Status of the reservation.
     * @param reservationDate        Reservation date in Gregorian calendar.
     * @param persianToGregorianDate Reservation date in Persian calendar converted to Gregorian.
     * @param startTime              Start time of the reservation.
     * @param endTime                End time of the reservation.
     * @return true if the reservation matches the filter, otherwise false.
     */
    private boolean matchesFilter(Reservation reservation, UUID userId, UUID serviceId, UUID providerId, ReservationStatus status,
                                  LocalDate reservationDate, LocalDate persianToGregorianDate, LocalTime startTime,
                                  LocalTime endTime, String referenceNumber) {
        return (userId == null || reservation.getUserId().equals(userId)) &&
                (serviceId == null || reservation.getServiceId().equals(serviceId)) &&
                (providerId == null || reservation.getProviderId().equals(providerId)) &&
                (status == null || reservation.getStatus() == status) &&
                (reservationDate == null || reservation.getReservationDate().equals(reservationDate)) &&
                (persianToGregorianDate == null || reservation.getReservationDate().equals(persianToGregorianDate)) &&
                (startTime == null || reservation.getStartTime().equals(startTime)) &&
                (endTime == null || reservation.getEndTime().equals(endTime));
    }

    /**
     *
     * @param existingReservation The reservation to update.
     * @param reservationDTO      The new details to update the reservation with.
     */
    private void updateReservationDetails(Reservation existingReservation, ReservationDTO reservationDTO) {
        existingReservation.setReservationDate(reservationDTO.getReservationDate());
        existingReservation.setStartTime(reservationDTO.getStartTime());
        existingReservation.setEndTime(reservationDTO.getEndTime());
        if (reservationDTO.getStatus() != null) {
            existingReservation.setStatus(reservationDTO.getStatus());
        }
        existingReservation.setUserId(reservationDTO.getUserId());
        existingReservation.setServiceId(reservationDTO.getServiceId());
        if (reservationDTO.getSlotId() != null) {
            existingReservation.setSlotId(reservationDTO.getSlotId());
        }
    }

    /**
     * Converts a Reservation entity to a ReservationDTO.
     *
     * @param reservation The Reservation entity to be converted.
     * @return The converted ReservationDTO.
     */
    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = reservationMapper.toDTO(reservation);
        dto.setReservationDatePersian(reservation.getReservationDatePersian().toString());
        return dto;
    }

    /**
     * Converts a ReservationDTO to a Reservation entity.
     *
     * @param reservationDTO The DTO to be converted.
     * @return The converted Reservation entity.
     */
    private Reservation convertToEntity(ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation.setReservationDatePersian(PersianDate.parse(reservationDTO.getReservationDatePersian()));
        reservation.setStatus(ReservationStatus.PENDING);
        return reservation;
    }

    /**
     * Emits a real-time notification reflecting the reservation slot's latest status.
     *
     * @param reservation reservation containing slot metadata.
     */
    private void notifyStatusChange(Reservation reservation) {
        if (reservation.getSlotId() == null || reservation.getServiceId() == null || reservation.getStatus() == null) {
            return;
        }
        SlotStatusNotification notification = SlotStatusNotification.builder()
                .slotId(extractRawSlotId(reservation.getSlotId()))
                .compositeSlotId(reservation.getSlotId())
                .serviceId(reservation.getServiceId())
                .status(reservation.getStatus())
                .build();
        slotStatusNotifier.publish(notification);
    }

    /**
     * Removes the service prefix from the composite slot identifier.
     *
     * @param compositeSlotId combined service and slot identifier.
     * @return slot identifier without the service prefix.
     */
    private String extractRawSlotId(String compositeSlotId) {
        int separatorIndex = compositeSlotId.indexOf(':');
        if (separatorIndex < 0) {
            return compositeSlotId;
        }
        return compositeSlotId.substring(separatorIndex + 1);
    }

    /**
     * Removes the basket entry associated with the provided identifiers without emitting slot notifications.
     *
     * @param userId identifier of the reservation owner
     * @param slotId slot identifier stored in the reservation
     */
    private void removeBasketEntry(UUID userId, String slotId) {
        if (userId == null || slotId == null) {
            return;
        }
        try {
            basketItemRepository.deleteByUserIdAndSlotId(userId, slotId);
        } catch (Exception exception) {
            logger.warn("Unable to delete basket entry for user {} and slot {} while finalizing reservations.", userId, slotId, exception);
        }
    }
}