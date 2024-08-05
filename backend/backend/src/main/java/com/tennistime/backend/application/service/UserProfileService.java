package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.UserProfile;
import com.tennistime.backend.domain.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public Optional<UserProfile> getUserProfile(Long id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile createUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public Optional<UserProfile> updateUserProfile(Long id, UserProfile updatedProfile) {
        return userProfileRepository.findById(id).map(userProfile -> {
            userProfile.setFirstName(updatedProfile.getFirstName());
            userProfile.setLastName(updatedProfile.getLastName());
            userProfile.setPhoneNumber(updatedProfile.getPhoneNumber());
            userProfile.setAddress(updatedProfile.getAddress());
            userProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
            userProfile.setProfilePicture(updatedProfile.getProfilePicture());
            userProfile.setPreferences(updatedProfile.getPreferences());
            return userProfileRepository.save(userProfile);
        });
    }

    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }
}
