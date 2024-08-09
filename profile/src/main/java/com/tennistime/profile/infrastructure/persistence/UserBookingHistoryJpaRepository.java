package com.tennistime.profile.infrastructure.persistence;

import com.tennistime.profile.domain.model.UserBookingHistory;
import com.tennistime.profile.domain.repository.UserBookingHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * JPA repository interface for UserBookingHistory entities.
 * Extends JpaRepository to provide standard CRUD operations and custom queries defined in UserBookingHistoryRepository.
 */
@Repository
public interface UserBookingHistoryJpaRepository extends UserBookingHistoryRepository, JpaRepository<UserBookingHistory, Long> {

    /**
     * Finds all booking history records for a specific user.
     *
     * @param userId UUID of the user whose booking history records are being retrieved.
     * @return List of UserBookingHistory associated with the specified user.
     */
    @Override
    List<UserBookingHistory> findByUserId(UUID userId);
}
