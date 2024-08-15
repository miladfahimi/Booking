package com.tennistime.backend.infrastructure.persistence;


import com.tennistime.backend.domain.model.Club;
import com.tennistime.backend.domain.repository.ClubRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClubJpaRepository extends ClubRepository, JpaRepository<Club, UUID> {
}
