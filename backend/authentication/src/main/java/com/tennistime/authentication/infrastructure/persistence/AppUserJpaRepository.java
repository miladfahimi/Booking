package com.tennistime.authentication.infrastructure.persistence;


import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.repository.AppUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserJpaRepository extends JpaRepository<AppUser, Long>, AppUserRepository {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByPhone(String phone);
    Optional<AppUser> findByUsername(String username);
}
