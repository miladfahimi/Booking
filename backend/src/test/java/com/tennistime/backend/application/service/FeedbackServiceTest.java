package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.domain.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByClubId_shouldReturnFeedbacksForClub() {
        Feedback feedback = new Feedback();
        when(feedbackRepository.findByClubId(anyLong())).thenReturn(Arrays.asList(feedback));

        List<Feedback> feedbacks = feedbackService.findByClubId(1L);
        assertEquals(1, feedbacks.size());
    }

    @Test
    void findByCourtId_shouldReturnFeedbacksForCourt() {
        Feedback feedback = new Feedback();
        when(feedbackRepository.findByCourtId(anyLong())).thenReturn(Arrays.asList(feedback));

        List<Feedback> feedbacks = feedbackService.findByCourtId(1L);
        assertEquals(1, feedbacks.size());
    }

    @Test
    void save_shouldSaveAndReturnFeedback() {
        Feedback feedback = new Feedback();
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        Feedback savedFeedback = feedbackService.save(feedback);
        assertEquals(feedback, savedFeedback);
    }
}
