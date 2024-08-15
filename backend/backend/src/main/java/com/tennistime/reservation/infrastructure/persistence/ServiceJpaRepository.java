package com.tennistime.reservation.infrastructure.persistence;


import com.tennistime.reservation.domain.model.Service;
import com.tennistime.reservation.domain.repository.ServiceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceJpaRepository extends ServiceRepository, JpaRepository<Service, UUID> {
}
