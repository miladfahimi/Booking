package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Reservation entities.
 * Provides methods for performing CRUD operations and custom queries related to reservations.
 */
public interface ReservationRepository {

    /**
     * Retrieves all reservation records in the database.
     *
     * @return List of all Reservation entities.
     */
    List<Reservation> findAll();

    /**
     * Finds a specific reservation by its UUID.
     *
     * @param id UUID of the reservation to retrieve.
     * @return Optional containing the found Reservation entity, or empty if not found.
     */
    Optional<Reservation> findById(UUID id); // Updated from UUID to UUID

    /**
     * Finds all reservations made by a specific user.
     *
     * @param userId UUID of the user whose reservations are being retrieved.
     * @return List of Reservation entities associated with the specified user.
     */
    List<Reservation> findByUserId(UUID userId);

    /**
     * Finds all reservations for a specific court.
     *
     * @param courtId ID of the court whose reservations are being retrieved.
     * @return List of Reservation entities associated with the specified court.
     */
    List<Reservation> findByCourtId(UUID courtId);

    /**
     * Finds all reservations made on a specific date.
     *
     * @param localDate The date to filter reservations by.
     * @return List of Reservation entities on the specified date.
     */
    List<Reservation> findByReservationDate(LocalDate localDate);

    /**
     * Saves a reservation record to the database.
     *
     * @param reservation The Reservation entity to save.
     * @return The saved Reservation entity.
     */
    Reservation save(Reservation reservation);

    /**
     * Deletes a specific reservation by its UUID.
     *
     * @param id UUID of the reservation to delete.
     */
    void deleteById(UUID id); // Updated from UUID to UUID
}
