package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Reservation;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository {
    List<Reservation> findAll();
    Reservation save(Reservation reservation);
    void deleteById(Long id);
}

