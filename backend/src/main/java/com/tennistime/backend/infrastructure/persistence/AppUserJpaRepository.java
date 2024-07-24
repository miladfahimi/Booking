package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.repository.AppUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserJpaRepository extends JpaRepository<AppUser, Long>, AppUserRepository {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByPhone(String phone); // Added method
}
