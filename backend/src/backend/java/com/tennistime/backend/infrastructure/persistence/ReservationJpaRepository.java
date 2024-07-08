package com.tennistime.backend.infrastructure.persistence.jpa;

import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationJpaRepository extends ReservationRepository, JpaRepository<Reservation, Long> {
}
