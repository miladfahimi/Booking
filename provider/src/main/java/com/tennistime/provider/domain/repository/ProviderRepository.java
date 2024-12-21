package com.tennistime.provider.domain.repository;

import com.tennistime.provider.domain.model.Provider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderRepository {
    List<Provider> findAll();
    Optional<Provider> findById(UUID id);
    Provider save(Provider provider);
    void deleteById(UUID id);
}
