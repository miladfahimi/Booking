package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Court;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourtRepository {
    List<Court> findAll();
    Optional<Court> findById(UUID id);
    Court save(Court court);
    void deleteById(UUID id);
}
