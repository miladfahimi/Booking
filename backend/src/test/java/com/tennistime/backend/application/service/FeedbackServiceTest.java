package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.FeedbackDTO;
import com.tennistime.backend.application.mapper.FeedbackMapper;
import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.domain.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private FeedbackMapper feedbackMapper;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback feedback;
    private FeedbackDTO feedbackDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feedback = new Feedback();
        feedback.setComment("Test Comment");
        feedback.setRating(5);

        feedbackDTO = new FeedbackDTO();
        feedbackDTO.setComment("Test Comment");
        feedbackDTO.setRating(5);
    }

    @Test
    void findAll_shouldReturnAllFeedbacks() {
        when(feedbackRepository.findAll()).thenReturn(Arrays.asList(feedback));
        when(feedbackMapper.toDTO(any(Feedback.class))).thenReturn(feedbackDTO);

        assertEquals(1, feedbackService.findAll().size());
    }

    @Test
    void save_shouldSaveAndReturnFeedback() {
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);
        when(feedbackMapper.toEntity(any(FeedbackDTO.class))).thenReturn(feedback);
        when(feedbackMapper.toDTO(any(Feedback.class))).thenReturn(feedbackDTO);

        FeedbackDTO savedFeedback = feedbackService.save(feedbackDTO);
        assertEquals(feedbackDTO, savedFeedback);
    }

    @Test
    void findById_shouldReturnFeedbackWhenExists() {
        when(feedbackRepository.findById(anyLong())).thenReturn(Optional.of(feedback));
        when(feedbackMapper.toDTO(any(Feedback.class))).thenReturn(feedbackDTO);

        FeedbackDTO foundFeedback = feedbackService.findById(1L);
        assertEquals(feedbackDTO, foundFeedback);
    }

    @Test
    void findById_shouldReturnNullWhenNotExists() {
        when(feedbackRepository.findById(anyLong())).thenReturn(Optional.empty());

        FeedbackDTO foundFeedback = feedbackService.findById(1L);
        assertNull(foundFeedback);
    }

    @Test
    void deleteById_shouldDeleteFeedback() {
        doNothing().when(feedbackRepository).deleteById(anyLong());

        feedbackService.deleteById(1L);
        verify(feedbackRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByClubId_shouldReturnFeedbackList() {
        when(feedbackRepository.findByClubId(anyLong())).thenReturn(Arrays.asList(feedback));
        when(feedbackMapper.toDTO(any(Feedback.class))).thenReturn(feedbackDTO);

        assertEquals(1, feedbackService.findByClubId(1L).size());
    }

    @Test
    void findByCourtId_shouldReturnFeedbackList() {
        when(feedbackRepository.findByCourtId(anyLong())).thenReturn(Arrays.asList(feedback));
        when(feedbackMapper.toDTO(any(Feedback.class))).thenReturn(feedbackDTO);

        assertEquals(1, feedbackService.findByCourtId(1L).size());
    }

    @Test
    void findTopRatedFeedbacksByClub_shouldReturnTopRatedFeedbackList() {
        when(feedbackRepository.findByClubId(anyLong())).thenReturn(Arrays.asList(feedback));
        when(feedbackMapper.toDTO(any(Feedback.class))).thenReturn(feedbackDTO);

        assertEquals(1, feedbackService.findTopRatedFeedbacksByClub(1L).size());
    }

    @Test
    void findTopRatedFeedbacksByCourt_shouldReturnTopRatedFeedbackList() {
        when(feedbackRepository.findByCourtId(anyLong())).thenReturn(Arrays.asList(feedback));
        when(feedbackMapper.toDTO(any(Feedback.class))).thenReturn(feedbackDTO);

        assertEquals(1, feedbackService.findTopRatedFeedbacksByCourt(1L).size());
    }
}
