package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserBookingHistory;

import java.util.List;
import java.util.Optional;

public interface UserBookingHistoryRepository {

    List<UserBookingHistory> findAll();
    Optional<UserBookingHistory> findById(Long id);
    List<UserBookingHistory> findByUserProfileId(Long userProfileId);  // Update the method signature
    UserBookingHistory save(UserBookingHistory userBookingHistory);
    void deleteById(Long id);
}
