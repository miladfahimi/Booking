package com.tennistime.profile.domain.repository;

import com.tennistime.profile.domain.model.UserBookingHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserBookingHistoryRepository {
    List<UserBookingHistory> findByUserId(UUID userId);
    List<UserBookingHistory> findAll();
    Optional<UserBookingHistory> findById(Long id);
    UserBookingHistory save(UserBookingHistory userBookingHistory);
    void deleteById(Long id);
}
