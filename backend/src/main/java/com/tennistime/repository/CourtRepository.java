package com.tennistime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tennistime.model.Court;

public interface CourtRepository extends JpaRepository<Court, Long> {
}
