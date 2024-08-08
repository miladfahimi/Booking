package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationJpaRepository extends ReservationRepository, JpaRepository<Reservation, UUID> { // Updated from Long to UUID
    @Override
    List<Reservation> findByUserId(UUID userId);
}
