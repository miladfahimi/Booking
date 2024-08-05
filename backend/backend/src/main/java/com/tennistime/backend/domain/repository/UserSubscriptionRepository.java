package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserSubscription;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository {
    Optional<UserSubscription> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    UserSubscription save(UserSubscription userSubscription);
}
