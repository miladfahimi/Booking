package com.tennistime.backend.infrastructure.persistence;


import com.tennistime.backend.domain.model.Service;
import com.tennistime.backend.domain.repository.ServiceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceJpaRepository extends ServiceRepository, JpaRepository<Service, UUID> {
}
