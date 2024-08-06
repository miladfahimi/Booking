package com.tennistime.backend.application.dto.userDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Schema(description = "Start date of the subscription in Persian calendar", example = "1402-10-11")
    private String startDatePersian;

    @Schema(description = "End date of the subscription in Persian calendar", example = "1403-10-11")
    private String endDatePersian;
}
