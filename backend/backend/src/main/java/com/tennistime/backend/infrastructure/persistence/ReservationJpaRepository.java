package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * JPA repository interface for Reservation entities.
 * Extends JpaRepository to provide standard CRUD operations and custom queries defined in ReservationRepository.
 */
@Repository
public interface ReservationJpaRepository extends ReservationRepository, JpaRepository<Reservation, UUID> { // Updated from Long to UUID

    /**
     * Finds all reservations made by a specific user.
     *
     * @param userId UUID of the user whose reservations are being retrieved.
     * @return List of Reservation entities associated with the specified user.
     */
    @Override
    List<Reservation> findByUserId(UUID userId);
}
