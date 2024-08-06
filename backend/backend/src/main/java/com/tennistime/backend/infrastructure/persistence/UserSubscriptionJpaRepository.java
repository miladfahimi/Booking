package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.UserSubscription;
import com.tennistime.backend.domain.repository.UserSubscriptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSubscriptionJpaRepository extends UserSubscriptionRepository, JpaRepository<UserSubscription, Long> {
    @Override
    Optional<UserSubscription> findByUserId(UUID userId);
}
