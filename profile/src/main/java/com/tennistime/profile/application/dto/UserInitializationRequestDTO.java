package com.tennistime.profile.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Initialization Request Data Transfer Object")
public class UserInitializationRequestDTO {

    @Schema(description = "User profile information")
    private UserProfileDTO userProfileDTO;

    @Schema(description = "User subscription information")
    private UserSubscriptionDTO userSubscriptionDTO;

    @Schema(description = "User booking history information")
    private List<UserBookingHistoryDTO> userBookingHistoryDTO;
}
