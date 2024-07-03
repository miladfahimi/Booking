package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.Court;
import java.util.List;

public interface CourtRepository {
    List<Court> findAll();
    Court save(Court court);
}
