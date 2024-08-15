package com.tennistime.backend.infrastructure.persistence;


import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.domain.repository.FeedbackRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackJpaRepository extends FeedbackRepository, JpaRepository<Feedback, Long> {
    @Override
    List<Feedback> findByProviderId(UUID providerId);

    @Override
    List<Feedback> findByCourtId(UUID courtId);

}
