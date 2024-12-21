package com.tennistime.provider.domain.repository;

import com.tennistime.provider.domain.model.Feedback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeedbackRepository {
    List<Feedback> findAll();
    Optional<Feedback> findById(UUID id);
    List<Feedback> findByProviderId(UUID providerId);
    List<Feedback> findByServiceId(UUID serviceId);
    Feedback save(Feedback feedback);
    void deleteById(UUID id);
}
