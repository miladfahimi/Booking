package com.tennistime.authentication.domain.repository;

import com.tennistime.authentication.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(UUID id);  // Changed from Long to UUID
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone); // Added method
    User save(User user);
    void deleteById(UUID id);  // Changed from Long to UUID
}
