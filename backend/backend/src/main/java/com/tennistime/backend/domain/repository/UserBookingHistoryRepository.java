package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserBookingHistory;
import com.tennistime.backend.domain.model.UserProfile;

import java.util.Optional;

public interface UserBookingHistoryRepository {
    Optional<UserBookingHistory> findById(Long id);
    UserBookingHistory save(UserBookingHistory userBookingHistory);
    void deleteById(Long id);
    Optional<UserBookingHistory> findByUserId(Long userId); // Add this method
}
