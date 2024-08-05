package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.UserSubscription;
import com.tennistime.backend.domain.repository.UserSubscriptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSubscriptionJpaRepository extends UserSubscriptionRepository, JpaRepository<UserSubscription, Long> {
    Optional<UserSubscription> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
