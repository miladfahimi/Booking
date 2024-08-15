package com.tennistime.reservation.infrastructure.persistence;


import com.tennistime.reservation.domain.model.Provider;
import com.tennistime.reservation.domain.repository.ProviderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProviderJpaRepository extends ProviderRepository, JpaRepository<Provider, UUID> {
}
