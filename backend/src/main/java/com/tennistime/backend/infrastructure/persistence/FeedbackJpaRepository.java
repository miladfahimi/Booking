package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.domain.repository.FeedbackRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackJpaRepository extends FeedbackRepository, JpaRepository<Feedback, Long> {
}
