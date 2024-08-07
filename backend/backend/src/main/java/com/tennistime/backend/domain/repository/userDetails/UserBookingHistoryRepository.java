package com.tennistime.backend.domain.repository.userDetails;

import com.tennistime.backend.domain.model.userDetails.UserBookingHistory;

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
