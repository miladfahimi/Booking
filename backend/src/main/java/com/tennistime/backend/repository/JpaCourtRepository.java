package com.tennistime.backend.repository;

import com.tennistime.backend.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCourtRepository extends JpaRepository<Court, Long> {
}
