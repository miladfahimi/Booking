package com.tennistime.authentication.application.service;

import com.tennistime.authentication.application.dto.UserDTO;
import com.tennistime.authentication.application.mapper.AppUserMapper;
import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing AppUser entities.
 */
@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Find all users.
     *
     * @return list of AppUserDTO
     */
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Save a new user.
     *
     * @param userDTO the user to save
     * @return the saved user
     */
    public UserDTO save(UserDTO userDTO) {
        User user = appUserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return appUserMapper.toDTO(user);
    }

    /**
     * Find user by ID.
     *
     * @param id the user ID
     * @return the user, or null if not found
     */
    public UserDTO findById(UUID id) {  // Changed from Long to UUID
        return userRepository.findById(id)
                .map(appUserMapper::toDTO)
                .orElse(null);
    }

    /**
     * Delete user by ID.
     *
     * @param id the user ID
     */
    public void deleteById(UUID id) {  // Changed from Long to UUID
        userRepository.deleteById(id);
    }
}
