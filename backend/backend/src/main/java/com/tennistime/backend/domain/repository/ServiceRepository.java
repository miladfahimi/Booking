package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository {
    List<Service> findAll();
    Optional<Service> findById(UUID id);
    Service save(Service service);
    void deleteById(UUID id);
}
