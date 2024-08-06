package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.mapper.ReservationMapper;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import com.tennistime.backend.domain.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing reservations.
 */
@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    public List<ReservationDTO> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReservationDTO findById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(this::convertToDTO).orElse(null);
    }

    public ReservationDTO save(ReservationDTO reservationDTO) {
        Reservation reservation = convertToEntity(reservationDTO);
        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDTO(savedReservation);
    }

    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setReservationDate(reservationDTO.getReservationDate());
            reservation.setStartTime(reservationDTO.getStartTime());
            reservation.setEndTime(reservationDTO.getEndTime());
            reservation.setStatus(reservationDTO.getStatus());
            reservation.setUserProfileId(reservationDTO.getUserProfileId());
            Optional<Court> courtOpt = courtRepository.findById(reservationDTO.getCourtId());
            courtOpt.ifPresent(reservation::setCourt);
            Reservation updatedReservation = reservationRepository.save(reservation);
            return convertToDTO(updatedReservation);
        }
        return null;
    }

    public ReservationDTO updateReservationStatus(Long id, String status) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setStatus(status);
            Reservation updatedReservation = reservationRepository.save(reservation);
            return convertToDTO(updatedReservation);
        }
        return null;
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationDTO> findReservationsByUserId(UUID userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> findReservationsByCourtId(Long courtId) {
        return reservationRepository.findByCourtId(courtId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> findReservationsByPersianDate(String persianDateStr) {
        LocalDate gregorianDate = PersianDate.parse(persianDateStr).toGregorian();
        return reservationRepository.findAll()
                .stream()
                .filter(reservation -> gregorianDate.equals(reservation.getReservationDate()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = reservationMapper.toDTO(reservation);
        dto.setReservationDatePersian(reservation.getReservationDatePersian().toString());
        return dto;
    }

    private Reservation convertToEntity(ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation.setReservationDatePersian(PersianDate.parse(reservationDTO.getReservationDatePersian()));
        return reservation;
    }
}
