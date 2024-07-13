package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.FeedbackDTO;
import com.tennistime.backend.application.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacks() {
        List<FeedbackDTO> feedbacks = feedbackService.findAll();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        FeedbackDTO feedback = feedbackService.findById(id);
        return feedback != null ? ResponseEntity.ok(feedback) : ResponseEntity.notFound().build();
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByClubId(@PathVariable Long clubId) {
        List<FeedbackDTO> feedbacks = feedbackService.findByClubId(clubId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/court/{courtId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByCourtId(@PathVariable Long courtId) {
        List<FeedbackDTO> feedbacks = feedbackService.findByCourtId(courtId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/club/{clubId}/top-rated")
    public ResponseEntity<List<FeedbackDTO>> getTopRatedFeedbacksByClub(@PathVariable Long clubId) {
        List<FeedbackDTO> feedbacks = feedbackService.findTopRatedFeedbacksByClub(clubId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/court/{courtId}/top-rated")
    public ResponseEntity<List<FeedbackDTO>> getTopRatedFeedbacksByCourt(@PathVariable Long courtId) {
        List<FeedbackDTO> feedbacks = feedbackService.findTopRatedFeedbacksByCourt(courtId);
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        FeedbackDTO createdFeedback = feedbackService.save(feedbackDTO);
        return ResponseEntity.ok(createdFeedback);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
