package com.tennistime.backend.repository;

import com.tennistime.backend.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<Court, Long> {
}
