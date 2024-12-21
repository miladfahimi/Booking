package com.tennistime.provider.application.service;

import com.tennistime.provider.application.dto.FeedbackDTO;
import com.tennistime.provider.application.mapper.FeedbackMapper;
import com.tennistime.provider.domain.model.Feedback;
import com.tennistime.provider.domain.repository.FeedbackRepository;
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

    public FeedbackDTO findById(UUID id) {
        Optional<Feedback> feedback = feedbackRepository.findById(id);
        return feedback.map(feedbackMapper::toDTO).orElse(null);
    }

    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDTO(savedFeedback);
    }

    public void deleteById(UUID id) {
        feedbackRepository.deleteById(id);
    }

    public List<FeedbackDTO> findByProviderId(UUID ProviderId) {
        return feedbackRepository.findByProviderId(ProviderId)
                .stream()
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackDTO> findByServiceId(UUID serviceId) {
        return feedbackRepository.findByServiceId(serviceId)
                .stream()
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackDTO> findTopRatedFeedbacksByProvider(UUID ProviderId) {
        return feedbackRepository.findByProviderId(ProviderId)
                .stream()
                .sorted((f1, f2) -> Integer.compare(f2.getRating(), f1.getRating()))
                .limit(10)
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackDTO> findTopRatedFeedbacksByService(UUID serviceId) {
        return feedbackRepository.findByServiceId(serviceId)
                .stream()
                .sorted((f1, f2) -> Integer.compare(f2.getRating(), f1.getRating()))
                .limit(10)
                .map(feedbackMapper::toDTO)
                .collect(Collectors.toList());
    }
}
