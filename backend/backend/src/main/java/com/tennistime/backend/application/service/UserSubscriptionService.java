package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.userDetails.UserSubscriptionDTO;
import com.tennistime.backend.application.mapper.UserSubscriptionMapper;
import com.tennistime.backend.domain.model.UserSubscription;
import com.tennistime.backend.domain.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
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

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserSubscriptionMapper userSubscriptionMapper;

    /**
     * Retrieves a user subscription by its ID.
     *
     * @param id the ID of the user subscription
     * @return an Optional containing the UserSubscriptionDTO if found, or empty if not found
     */
    public Optional<UserSubscriptionDTO> getUserSubscription(Long id) {
        return userSubscriptionRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Creates a new user subscription.
     *
     * @param userSubscriptionDTO the DTO containing the user subscription data
     * @return the created UserSubscriptionDTO
     */
    public UserSubscriptionDTO createUserSubscription(UserSubscriptionDTO userSubscriptionDTO) {
        UserSubscription userSubscription = convertToEntity(userSubscriptionDTO);
        UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);
        return convertToDTO(savedUserSubscription);
    }

    /**
     * Updates an existing user subscription.
     *
     * @param id the ID of the user subscription to update
     * @param updatedSubscriptionDTO the DTO containing the updated subscription data
     * @return an Optional containing the updated UserSubscriptionDTO if the update was successful, or empty if not
     */
    public Optional<UserSubscriptionDTO> updateUserSubscription(Long id, UserSubscriptionDTO updatedSubscriptionDTO) {
        return userSubscriptionRepository.findById(id)
                .map(existingSubscription -> {
                    existingSubscription.setSubscriptionPlan(updatedSubscriptionDTO.getSubscriptionPlan());
                    existingSubscription.setStartDate(LocalDate.parse(updatedSubscriptionDTO.getStartDate()));
                    existingSubscription.setEndDate(LocalDate.parse(updatedSubscriptionDTO.getEndDate()));
                    existingSubscription.setStatus(updatedSubscriptionDTO.getStatus());
                    existingSubscription.setStartDatePersian(
                            updatedSubscriptionDTO.getStartDatePersian() != null && !updatedSubscriptionDTO.getStartDatePersian().equals("Does not filled by User")
                                    ? PersianDate.parse(updatedSubscriptionDTO.getStartDatePersian())
                                    : null
                    );
                    existingSubscription.setEndDatePersian(
                            updatedSubscriptionDTO.getEndDatePersian() != null && !updatedSubscriptionDTO.getEndDatePersian().equals("Does not filled by User")
                                    ? PersianDate.parse(updatedSubscriptionDTO.getEndDatePersian())
                                    : null
                    );
                    // Handle other fields if any...

                    UserSubscription savedSubscription = userSubscriptionRepository.save(existingSubscription);
                    return convertToDTO(savedSubscription);
                });
    }

    /**
     * Deletes a user subscription by its ID.
     *
     * @param id the ID of the user subscription to delete
     */
    public void deleteUserSubscription(Long id) {
        userSubscriptionRepository.deleteById(id);
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
