package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Club;
import java.util.List;

public interface ClubRepository {
    List<Club> findAll();
    Club save(Club club);
}
