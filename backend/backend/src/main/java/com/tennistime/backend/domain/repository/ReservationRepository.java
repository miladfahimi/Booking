package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    List<Reservation> findAll();
    Optional<Reservation> findById(UUID id); // Updated from Long to UUID
    List<Reservation> findByUserId(UUID userId);
    List<Reservation> findByCourtId(Long courtId);
    List<Reservation> findByReservationDate(LocalDate localDate);
    Reservation save(Reservation reservation);
    void deleteById(UUID id); // Updated from Long to UUID
}
