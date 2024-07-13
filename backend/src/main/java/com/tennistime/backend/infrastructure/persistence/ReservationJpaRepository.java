package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationJpaRepository extends ReservationRepository, JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
}
