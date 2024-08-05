package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserBookingHistory;

import java.util.List;
import java.util.Optional;

public interface UserBookingHistoryRepository {
    Optional<UserBookingHistory> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    UserBookingHistory save(UserBookingHistory userBookingHistory);
}
