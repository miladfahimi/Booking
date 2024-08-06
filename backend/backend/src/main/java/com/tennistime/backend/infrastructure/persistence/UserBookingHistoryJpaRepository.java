package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.UserBookingHistory;
import com.tennistime.backend.domain.repository.UserBookingHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookingHistoryJpaRepository extends UserBookingHistoryRepository, JpaRepository<UserBookingHistory, Long> {
    List<UserBookingHistory> findByUserProfileId(Long userProfileId);  // Update the method signature
}
