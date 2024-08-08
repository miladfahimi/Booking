package com.tennistime.profile.infrastructure.persistence;

import com.tennistime.profile.domain.model.UserSubscription;
import com.tennistime.profile.domain.repository.UserSubscriptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSubscriptionJpaRepository extends UserSubscriptionRepository, JpaRepository<UserSubscription, Long> {
    @Override
    Optional<UserSubscription> findByUserId(UUID userId);
}
