package com.tennistime.backend.application.dto.userDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Schema(description = "User Initialization Response Data Transfer Object")
public class UserInitializationResponseDTO {

    @Schema(description = "User ID")
    private UUID userId;

    @Schema(description = "Is user profiles initiated")
    private boolean isUserProfilesInitiated;

    @Schema(description = "User profile information")
    private UserProfileDTO userProfileDTO;

    @Schema(description = "User subscription information")
    private UserSubscriptionDTO userSubscriptionDTO;

    @Schema(description = "User booking history information")
    private List<UserBookingHistoryDTO> userBookingHistoryDTO;

    public UserInitializationResponseDTO(UUID userId, boolean isUserProfilesInitiated, UserProfileDTO userProfileDTO, UserSubscriptionDTO userSubscriptionDTO, List<UserBookingHistoryDTO> userBookingHistoryDTO) {
        this.userId = userId;
        this.isUserProfilesInitiated = isUserProfilesInitiated;
        this.userProfileDTO = userProfileDTO;
        this.userSubscriptionDTO = userSubscriptionDTO;
        this.userBookingHistoryDTO = userBookingHistoryDTO;
    }
}
