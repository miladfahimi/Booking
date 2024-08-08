package com.tennistime.profile.infrastructure.persistence;

import com.tennistime.profile.domain.model.UserBookingHistory;
import com.tennistime.profile.domain.repository.UserBookingHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserBookingHistoryJpaRepository extends UserBookingHistoryRepository, JpaRepository<UserBookingHistory, Long> {
    @Override
    List<UserBookingHistory> findByUserId(UUID userId);
}
