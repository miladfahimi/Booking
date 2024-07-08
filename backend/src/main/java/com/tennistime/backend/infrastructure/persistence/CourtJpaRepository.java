package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.CourtRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtJpaRepository extends CourtRepository, JpaRepository<Court, Long> {
}
