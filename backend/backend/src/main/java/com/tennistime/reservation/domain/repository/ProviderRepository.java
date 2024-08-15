package com.tennistime.reservation.domain.repository;

import com.tennistime.reservation.domain.model.Provider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderRepository {
    List<Provider> findAll();
    Optional<Provider> findById(UUID id);
    Provider save(Provider provider);
    void deleteById(UUID id);
}
