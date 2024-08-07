package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.UserBookingHistory;
import com.tennistime.backend.domain.repository.UserBookingHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserBookingHistoryJpaRepository extends UserBookingHistoryRepository, JpaRepository<UserBookingHistory, Long> {
    @Override
    List<UserBookingHistory> findByUserId(UUID userId);
}
