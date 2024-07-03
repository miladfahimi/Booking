package com.tennistime.backend.infrastructure.persistence.jpa;

import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.domain.repository.FeedbackRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackJpaRepository extends FeedbackRepository, JpaRepository<Feedback, Long> {
    List<Feedback> findByClubId(Long clubId);
    List<Feedback> findByCourtId(Long courtId);
}
