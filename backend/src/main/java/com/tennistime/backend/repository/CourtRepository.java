package com.tennistime.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tennistime.backend.model.Court;

public interface CourtRepository extends JpaRepository<Court, Long> {
}
