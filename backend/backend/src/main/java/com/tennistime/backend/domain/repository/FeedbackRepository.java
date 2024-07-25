package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository {
    List<Feedback> findAll();
    Optional<Feedback> findById(Long id);
    List<Feedback> findByClubId(Long clubId);
    List<Feedback> findByCourtId(Long courtId);
    Feedback save(Feedback feedback);
    void deleteById(Long id);
}
