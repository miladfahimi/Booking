package com.tennistime.authentication.domain.repository;


import com.tennistime.authentication.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone); // Added method
    User save(User user);
    void deleteById(Long id);
}
