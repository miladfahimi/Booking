package com.tennistime.provider.domain.repository;

import com.tennistime.provider.domain.model.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository {
    List<Service> findAll();
    Optional<Service> findById(UUID id);
    List<Service> findByProviderId(UUID providerId);
    Service save(Service service);
    void deleteById(UUID id);
}
