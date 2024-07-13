package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.domain.repository.FeedbackRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackJpaRepository extends FeedbackRepository, JpaRepository<Feedback, Long> {
    @Override
    List<Feedback> findByClubId(Long clubId);

    @Override
    List<Feedback> findByCourtId(Long courtId);
}
