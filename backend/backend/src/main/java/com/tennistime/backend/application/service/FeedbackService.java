package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.FeedbackDTO;
import com.tennistime.backend.application.mapper.FeedbackMapper;
import com.tennistime.backend.domain.model.Feedback;
import com.tennistime.backend.domain.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    public List<FeedbackDTO> findAll() {
        return feedbackRepository.findAll()
                .stream()
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FeedbackDTO findById(Long id) {
        Optional<Feedback> feedback = feedbackRepository.findById(id);
        return feedback.map(feedbackMapper::toDTO).orElse(null);
    }

    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDTO(savedFeedback);
    }

    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }

    public List<FeedbackDTO> findByClubId(UUID clubId) {
        return feedbackRepository.findByClubId(clubId)
                .stream()
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackDTO> findByCourtId(Long courtId) {
        return feedbackRepository.findByCourtId(courtId)
                .stream()
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackDTO> findTopRatedFeedbacksByClub(UUID clubId) {
        return feedbackRepository.findByClubId(clubId)
                .stream()
                .sorted((f1, f2) -> Integer.compare(f2.getRating(), f1.getRating()))
                .limit(10)
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackDTO> findTopRatedFeedbacksByCourt(Long courtId) {
        return feedbackRepository.findByCourtId(courtId)
                .stream()
                .sorted((f1, f2) -> Integer.compare(f2.getRating(), f1.getRating()))
                .limit(10)
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }
}
