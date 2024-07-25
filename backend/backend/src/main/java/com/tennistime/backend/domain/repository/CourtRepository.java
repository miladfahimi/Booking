package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Court;

import java.util.List;
import java.util.Optional;

public interface CourtRepository {
    List<Court> findAll();
    Optional<Court> findById(Long id);
    Court save(Court court);
    void deleteById(Long id);
}
