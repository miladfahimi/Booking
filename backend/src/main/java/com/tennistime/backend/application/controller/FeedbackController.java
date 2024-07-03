package com.tennistime.backend.application.controller;

import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.application.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/clubs/{clubId}")
    public List<Feedback> getFeedbackByClubId(@PathVariable Long clubId) {
        return feedbackService.findByClubId(clubId);
    }

    @GetMapping("/courts/{courtId}")
    public List<Feedback> getFeedbackByCourtId(@PathVariable Long courtId) {
        return feedbackService.findByCourtId(courtId);
    }

    @PostMapping
    public Feedback addFeedback(@RequestBody Feedback feedback) {
        return feedbackService.save(feedback);
    }
}
