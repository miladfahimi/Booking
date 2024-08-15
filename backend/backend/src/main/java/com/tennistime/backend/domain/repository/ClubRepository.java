package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Club;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClubRepository {
    List<Club> findAll();
    Optional<Club> findById(UUID id);
    Club save(Club club);
    void deleteById(UUID id);
}
