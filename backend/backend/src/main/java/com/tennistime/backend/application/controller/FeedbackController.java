package com.tennistime.backend.application.controller;


import com.tennistime.backend.application.dto.FeedbackDTO;
import com.tennistime.backend.application.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feedbacks")
@Tag(name = "Feedback Management", description = "Operations related to feedback management")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    @Operation(summary = "Get all feedbacks")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacks() {
        List<FeedbackDTO> feedbacks = feedbackService.findAll();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        FeedbackDTO feedback = feedbackService.findById(id);
        return feedback != null ? ResponseEntity.ok(feedback) : ResponseEntity.notFound().build();
    }

    @GetMapping("/club/{clubId}")
    @Operation(summary = "Get feedbacks by club ID")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByClubId(@PathVariable UUID clubId) {
        List<FeedbackDTO> feedbacks = feedbackService.findByClubId(clubId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/court/{courtId}")
    @Operation(summary = "Get feedbacks by court ID")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByCourtId(@PathVariable Long courtId) {
        List<FeedbackDTO> feedbacks = feedbackService.findByCourtId(courtId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/club/{clubId}/top-rated")
    @Operation(summary = "Get top-rated feedbacks by club ID")
    public ResponseEntity<List<FeedbackDTO>> getTopRatedFeedbacksByClub(@PathVariable UUID clubId) {
        List<FeedbackDTO> feedbacks = feedbackService.findTopRatedFeedbacksByClub(clubId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/court/{courtId}/top-rated")
    @Operation(summary = "Get top-rated feedbacks by court ID")
    public ResponseEntity<List<FeedbackDTO>> getTopRatedFeedbacksByCourt(@PathVariable Long courtId) {
        List<FeedbackDTO> feedbacks = feedbackService.findTopRatedFeedbacksByCourt(courtId);
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping
    @Operation(summary = "Create a new feedback")
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        FeedbackDTO createdFeedback = feedbackService.save(feedbackDTO);
        return ResponseEntity.ok(createdFeedback);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a feedback by ID")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
