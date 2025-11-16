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
import com.tennistime.reservation.infrastructure.payment.sep.SepPaymentProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UserBookingHistoryUpdater userBookingHistoryUpdater;
    private final SlotStatusNotifier slotStatusNotifier;
    private final ReservationBasketItemRepository basketItemRepository;
    private final SepPaymentProperties paymentProperties;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationMapper reservationMapper,
                              UserBookingHistoryUpdater userBookingHistoryUpdater,
                              SlotStatusNotifier slotStatusNotifier,
                              ReservationBasketItemRepository basketItemRepository,
                              SepPaymentProperties paymentProperties) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.userBookingHistoryUpdater = userBookingHistoryUpdater;
        this.slotStatusNotifier = slotStatusNotifier;
        this.basketItemRepository = basketItemRepository;
        this.paymentProperties = paymentProperties;
    }

    public List<ReservationDTO> findAll() {
        logger.info("Fetching all reservations.");
        return reservationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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

    public void releaseExpiredPendingReservations() {
        Integer expiryMinutes = paymentProperties.defaultTokenExpiryMinutes();
        if (expiryMinutes == null || expiryMinutes <= 0) {
            return;
        }
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(expiryMinutes);
        List<Reservation> expiredReservations = reservationRepository.findByStatusAndBookedAtBefore(
                ReservationStatus.PENDING, threshold);
        expiredReservations.forEach(reservation -> {
            reservation.setStatus(ReservationStatus.AVAILABLE);
            reservation.setExpirationDate(null);
            Reservation updated = reservationRepository.save(reservation);
            userBookingHistoryUpdater.updateUserBookingHistory(updated);
            notifyStatusChange(updated);
        });
    }

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

    public void deleteById(UUID id) {
        logger.info("Deleting reservation with ID: {}", id);
        reservationRepository.deleteById(id);
    }

    public List<ReservationDTO> findReservationsByUserId(UUID userId) {
        logger.info("Fetching reservations for user ID: {}", userId);
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> findReservationsByServiceId(UUID serviceId) {
        logger.info("Fetching reservations for service ID: {}", serviceId);
        return reservationRepository.findByServiceId(serviceId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> findReservationsByPersianDate(String persianDateStr) {
        logger.info("Fetching reservations for Persian date: {}", persianDateStr);
        LocalDate gregorianDate = PersianDate.parse(persianDateStr).toGregorian();
        return reservationRepository.findAll()
                .stream()
                .filter(reservation -> gregorianDate.equals(reservation.getReservationDate()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = reservationMapper.toDTO(reservation);
        dto.setReservationDatePersian(reservation.getReservationDatePersian().toString());
        return dto;
    }

    private Reservation convertToEntity(ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation.setReservationDatePersian(PersianDate.parse(reservationDTO.getReservationDatePersian()));
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setExpirationDate(calculateExpirationTimestamp());
        return reservation;
    }

    private LocalDateTime calculateExpirationTimestamp() {
        Integer expiryMinutes = paymentProperties.defaultTokenExpiryMinutes();
        if (expiryMinutes == null || expiryMinutes <= 0) {
            return null;
        }
        return LocalDateTime.now().plusMinutes(expiryMinutes);
    }

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

    private String extractRawSlotId(String compositeSlotId) {
        int separatorIndex = compositeSlotId.indexOf(':');
        if (separatorIndex < 0) {
            return compositeSlotId;
        }
        return compositeSlotId.substring(separatorIndex + 1);
    }

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
