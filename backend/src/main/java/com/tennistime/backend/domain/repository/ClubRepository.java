package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Club;
import java.util.List;
import java.util.Optional;

public interface ClubRepository {
    List<Club> findAll();
    Optional<Club> findById(Long id);
    Club save(Club club);
    void deleteById(Long id);
}
