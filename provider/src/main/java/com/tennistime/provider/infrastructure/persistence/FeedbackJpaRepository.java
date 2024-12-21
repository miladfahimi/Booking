package com.tennistime.provider.infrastructure.persistence;


import com.tennistime.provider.domain.model.Feedback;
import com.tennistime.provider.domain.repository.FeedbackRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackJpaRepository extends FeedbackRepository, JpaRepository<Feedback, UUID> {
    @Override
    List<Feedback> findByProviderId(UUID providerId);

    @Override
    List<Feedback> findByServiceId(UUID serviceId);

}
