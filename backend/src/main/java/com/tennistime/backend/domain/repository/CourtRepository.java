package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Court;
import java.util.List;
import java.util.Optional;

public interface CourtRepository {
    List<Court> findAll();
    Court save(Court court);
    Optional<Court> findById(Long id);
}
