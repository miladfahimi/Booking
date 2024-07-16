package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.mapper.ReservationMapper;
import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

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

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationDTO> findReservationsByUserId(Long userId) {
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
