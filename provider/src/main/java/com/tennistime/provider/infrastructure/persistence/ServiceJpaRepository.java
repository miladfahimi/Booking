package com.tennistime.provider.infrastructure.persistence;


import com.tennistime.provider.domain.model.Service;
import com.tennistime.provider.domain.repository.ServiceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceJpaRepository extends ServiceRepository, JpaRepository<Service, UUID> {
}
