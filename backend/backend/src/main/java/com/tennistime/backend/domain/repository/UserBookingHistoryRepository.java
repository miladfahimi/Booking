package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserBookingHistory;
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
