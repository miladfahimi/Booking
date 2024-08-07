package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.userDetails.UserSubscriptionDTO;
import com.tennistime.backend.application.mapper.UserSubscriptionMapper;
import com.tennistime.backend.domain.model.UserSubscription;
import com.tennistime.backend.domain.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing user subscriptions.
 */
@Service
@RequiredArgsConstructor
public class UserSubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionService.class);

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserSubscriptionMapper userSubscriptionMapper;

    public Optional<UserSubscriptionDTO> getUserSubscriptionByUserId(UUID userId) {
        logger.info("\033[0;34mFetching user subscription for user ID: {}\033[0m", userId);
        return userSubscriptionRepository.findByUserId(userId)
                .map(this::convertToDTO);
    }

    /**
     * Creates a new user subscription.
     *
     * @param userSubscriptionDTO the DTO containing the user subscription data
     * @return the created UserSubscriptionDTO
     */
    public UserSubscriptionDTO createUserSubscription(UserSubscriptionDTO userSubscriptionDTO) {
        logger.info("\033[0;32mCreating new user subscription: {}\033[0m", userSubscriptionDTO);
        UserSubscription userSubscription = convertToEntity(userSubscriptionDTO);
        UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);
        return convertToDTO(savedUserSubscription);
    }

    public Optional<UserSubscriptionDTO> updateUserSubscription(UUID userId, UserSubscriptionDTO updatedSubscriptionDTO) {
        logger.info("\033[0;33mUpdating user subscription for user ID: {}\033[0m", userId);
        return userSubscriptionRepository.findByUserId(userId)
                .map(existingSubscription -> {
                    UserSubscription updatedSubscription = convertToEntity(updatedSubscriptionDTO);
                    updatedSubscription.setId(existingSubscription.getId());
                    updatedSubscription.setUserId(userId); // Ensure the userId is set
                    UserSubscription savedUserSubscription = userSubscriptionRepository.save(updatedSubscription);
                    return convertToDTO(savedUserSubscription);
                });
    }

    public void deleteUserSubscriptionByUserId(UUID userId) {
        logger.info("\033[0;31mDeleting user subscription for user ID: {}\033[0m", userId);
        userSubscriptionRepository.findByUserId(userId)
                .ifPresent(userSubscription -> userSubscriptionRepository.deleteById(userSubscription.getId()));
    }

    /**
     * Converts a UserSubscription entity to a UserSubscriptionDTO.
     *
     * @param userSubscription the UserSubscription entity to convert
     * @return the converted UserSubscriptionDTO
     */
    private UserSubscriptionDTO convertToDTO(UserSubscription userSubscription) {
        UserSubscriptionDTO dto = userSubscriptionMapper.toDTO(userSubscription);
        dto.setStartDatePersian(userSubscription.getStartDatePersian() != null ? userSubscription.getStartDatePersian().toString() : "Does not filled by User");
        dto.setEndDatePersian(userSubscription.getEndDatePersian() != null ? userSubscription.getEndDatePersian().toString() : "Does not filled by User");
        return dto;
    }

    /**
     * Converts a UserSubscriptionDTO to a UserSubscription entity.
     *
     * @param userSubscriptionDTO the UserSubscriptionDTO to convert
     * @return the converted UserSubscription entity
     */
    private UserSubscription convertToEntity(UserSubscriptionDTO userSubscriptionDTO) {
        UserSubscription userSubscription = userSubscriptionMapper.toEntity(userSubscriptionDTO);
        userSubscription.setStartDatePersian(userSubscriptionDTO.getStartDatePersian() != null && !userSubscriptionDTO.getStartDatePersian().equals("Does not filled by User") ? PersianDate.parse(userSubscriptionDTO.getStartDatePersian()) : null);
        userSubscription.setEndDatePersian(userSubscriptionDTO.getEndDatePersian() != null && !userSubscriptionDTO.getEndDatePersian().equals("Does not filled by User") ? PersianDate.parse(userSubscriptionDTO.getEndDatePersian()) : null);
        return userSubscription;
    }
}
