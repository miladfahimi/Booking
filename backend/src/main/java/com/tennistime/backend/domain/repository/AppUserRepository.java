package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository {
    List<AppUser> findAll();
    Optional<AppUser> findById(Long id);
    AppUser save(AppUser appUser);
    void deleteById(Long id);
}
