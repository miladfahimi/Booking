package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();
    Optional<Reservation> findById(Long id);
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByCourtId(Long courtId);
    List<Reservation> findByReservationDate(LocalDate localDate);
    Reservation save(Reservation reservation);
    void deleteById(Long id);
}
