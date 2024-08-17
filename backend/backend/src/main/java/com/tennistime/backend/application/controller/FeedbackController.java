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
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable UUID id) {
        FeedbackDTO feedback = feedbackService.findById(id);
        return feedback != null ? ResponseEntity.ok(feedback) : ResponseEntity.notFound().build();
    }

    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Get feedbacks by provider ID")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByProviderId(@PathVariable UUID providerId) {
        List<FeedbackDTO> feedbacks = feedbackService.findByProviderId(providerId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/service/{serviceId}")
    @Operation(summary = "Get feedbacks by service ID")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByServiceId(@PathVariable UUID serviceId) {
        List<FeedbackDTO> feedbacks = feedbackService.findByServiceId(serviceId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/provider/{providerId}/top-rated")
    @Operation(summary = "Get top-rated feedbacks by provider ID")
    public ResponseEntity<List<FeedbackDTO>> getTopRatedFeedbacksByProvider(@PathVariable UUID providerId) {
        List<FeedbackDTO> feedbacks = feedbackService.findTopRatedFeedbacksByProvider(providerId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/service/{serviceId}/top-rated")
    @Operation(summary = "Get top-rated feedbacks by service ID")
    public ResponseEntity<List<FeedbackDTO>> getTopRatedFeedbacksByService(@PathVariable UUID serviceId) {
        List<FeedbackDTO> feedbacks = feedbackService.findTopRatedFeedbacksByService(serviceId);
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
    public ResponseEntity<Void> deleteFeedback(@PathVariable UUID id) {
        feedbackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
