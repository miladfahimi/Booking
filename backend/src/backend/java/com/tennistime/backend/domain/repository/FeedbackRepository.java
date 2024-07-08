package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Feedback;
import java.util.List;

public interface FeedbackRepository {
    List<Feedback> findByClubId(Long clubId);
    List<Feedback> findByCourtId(Long courtId);
    Feedback save(Feedback feedback);
}
