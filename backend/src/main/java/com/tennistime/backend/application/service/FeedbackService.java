package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.domain.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> findByClubId(Long clubId) {
        return feedbackRepository.findByClubId(clubId);
    }

    public List<Feedback> findByCourtId(Long courtId) {
        return feedbackRepository.findByCourtId(courtId);
    }

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
}
