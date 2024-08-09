package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.dto.UserBookingHistoryUpdateRequest;
import com.tennistime.backend.infrastructure.feign.UserBookingHistoryClient;
import com.tennistime.backend.application.mapper.ReservationMapper;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import com.tennistime.backend.domain.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate; // Import added for LocalDate
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing reservations.
 * This includes creating, updating, retrieving, and deleting reservation records.
 */
@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private UserBookingHistoryClient userBookingHistoryClient;

    /**
     * Retrieves all reservation records.
     *
     * @return List of ReservationDTO representing all reservations.
     */
    public List<ReservationDTO> findAll() {
        logger.info("[ReservationService][FindAll]: Fetching all reservations.");
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
        logger.info("[ReservationService][FindById]: Fetching reservation by ID: {}", id);
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(this::convertToDTO).orElse(null);
    }

    /**
     * Creates a new reservation record.
     *
     * @param reservationDTO DTO containing the details of the reservation to create.
     * @return The created ReservationDTO.
     */
    public ReservationDTO save(ReservationDTO reservationDTO) {
        logger.info("[ReservationService][Save]: Creating new reservation.");

        // Retrieve the Court entity based on the courtId from the reservationDTO
        Optional<Court> courtOpt = courtRepository.findById(reservationDTO.getCourtId());

        if (courtOpt.isEmpty()) {
            logger.error("[ReservationService][Save]: Court with ID: {} not found.", reservationDTO.getCourtId());
            throw new RuntimeException("Court not found");
        }

        // Convert DTO to Reservation entity and set the court
        Reservation reservation = convertToEntity(reservationDTO);
        reservation.setCourt(courtOpt.get());

        Reservation savedReservation = reservationRepository.save(reservation);

        // Format booking date to ISO_LOCAL_DATE_TIME
        String bookingDate = savedReservation.getReservationDate().atTime(savedReservation.getStartTime()).toString();

        // Prepare and send update to UserBookingHistory in Profile Service
        UserBookingHistoryUpdateRequest updateRequest = new UserBookingHistoryUpdateRequest(
                savedReservation.getUserId(),
                savedReservation.getId(),
                savedReservation.getCourt().getId(),  // Ensure the court ID is set
                bookingDate,
                savedReservation.getStatus(),
                savedReservation.getReservationDatePersian().toString(),
                false,
                false,
                null
        );

        try {
            logger.info("Sending UserBookingHistoryUpdateRequest to Profile Service: {}", updateRequest);
            userBookingHistoryClient.updateUserBookingHistory(updateRequest);
            logger.info("[ReservationService][UserBookingHistoryUpdate]: Successfully updated UserBookingHistory for reservation ID: {}", savedReservation.getId());
        } catch (Exception e) {
            logger.error("[ReservationService][UserBookingHistoryUpdate]: Failed to update UserBookingHistory for reservation ID: {} - Error: {}", savedReservation.getId(), e.getMessage());
            // Implement fallback or retry logic as needed
        }

        return convertToDTO(savedReservation);
    }

    /**
     * Updates an existing reservation record.
     *
     * @param id UUID of the reservation to update.
     * @param reservationDTO DTO containing the updated details of the reservation.
     * @return The updated ReservationDTO, or null if the reservation was not found.
     */
    public ReservationDTO updateReservation(UUID id, ReservationDTO reservationDTO) {
        logger.info("[ReservationService][Update]: Updating reservation with ID: {}", id);
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setReservationDate(reservationDTO.getReservationDate());
            reservation.setStartTime(reservationDTO.getStartTime());
            reservation.setEndTime(reservationDTO.getEndTime());
            reservation.setStatus(reservationDTO.getStatus());
            reservation.setUserId(reservationDTO.getUserId());
            Optional<Court> courtOpt = courtRepository.findById(reservationDTO.getCourtId());
            courtOpt.ifPresent(reservation::setCourt);
            Reservation updatedReservation = reservationRepository.save(reservation);
            return convertToDTO(updatedReservation);
        }
        logger.warn("[ReservationService][Update]: Reservation with ID: {} not found.", id);
        return null;
    }

    /**
     * Updates the status of an existing reservation.
     *
     * @param id UUID of the reservation to update.
     * @param status New status for the reservation.
     * @return The updated ReservationDTO, or null if the reservation was not found.
     */
    public ReservationDTO updateReservationStatus(UUID id, String status) {
        logger.info("[ReservationService][UpdateStatus]: Updating status of reservation with ID: {}", id);
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setStatus(status);
            Reservation updatedReservation = reservationRepository.save(reservation);
            return convertToDTO(updatedReservation);
        }
        logger.warn("[ReservationService][UpdateStatus]: Reservation with ID: {} not found.", id);
        return null;
    }

    /**
     * Deletes a reservation record by its UUID.
     *
     * @param id UUID of the reservation to delete.
     */
    public void deleteById(UUID id) {
        logger.info("[ReservationService][Delete]: Deleting reservation with ID: {}", id);
        reservationRepository.deleteById(id);
    }

    /**
     * Retrieves all reservations made by a specific user.
     *
     * @param userId UUID of the user whose reservations to retrieve.
     * @return List of ReservationDTO representing the user's reservations.
     */
    public List<ReservationDTO> findReservationsByUserId(UUID userId) {
        logger.info("[ReservationService][FindByUserId]: Fetching reservations for user ID: {}", userId);
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all reservations for a specific court.
     *
     * @param courtId ID of the court whose reservations to retrieve.
     * @return List of ReservationDTO representing the court's reservations.
     */
    public List<ReservationDTO> findReservationsByCourtId(Long courtId) {
        logger.info("[ReservationService][FindByCourtId]: Fetching reservations for court ID: {}", courtId);
        return reservationRepository.findByCourtId(courtId)
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
        logger.info("[ReservationService][FindByPersianDate]: Fetching reservations for Persian date: {}", persianDateStr);
        LocalDate gregorianDate = PersianDate.parse(persianDateStr).toGregorian();
        return reservationRepository.findAll()
                .stream()
                .filter(reservation -> gregorianDate.equals(reservation.getReservationDate()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
        return reservation;
    }
}
