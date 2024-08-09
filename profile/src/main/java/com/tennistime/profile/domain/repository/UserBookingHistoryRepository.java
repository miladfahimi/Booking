package com.tennistime.profile.domain.repository;

import com.tennistime.profile.domain.model.UserBookingHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing UserBookingHistory entities.
 * Provides methods for performing CRUD operations and custom queries related to user booking history.
 */
public interface UserBookingHistoryRepository {

    /**
     * Finds all booking history records for a specific user.
     *
     * @param userId UUID of the user whose booking history records are being retrieved.
     * @return List of UserBookingHistory associated with the specified user.
     */
    List<UserBookingHistory> findByUserId(UUID userId);

    /**
     * Retrieves all booking history records in the database.
     *
     * @return List of all UserBookingHistory entities.
     */
    List<UserBookingHistory> findAll();

    /**
     * Finds a specific booking history record by its ID.
     *
     * @param id ID of the booking history record to retrieve.
     * @return Optional containing the found UserBookingHistory entity, or empty if not found.
     */
    Optional<UserBookingHistory> findById(UUID id);

    /**
     * Saves a booking history record to the database.
     *
     * @param userBookingHistory The UserBookingHistory entity to save.
     * @return The saved UserBookingHistory entity.
     */
    UserBookingHistory save(UserBookingHistory userBookingHistory);

    /**
     * Deletes a specific booking history record by its ID.
     *
     * @param id ID of the booking history record to delete.
     */
    void deleteById(UUID id);
}
