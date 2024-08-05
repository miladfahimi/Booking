package com.tennistime.backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Profile Data Transfer Object")
public class UserProfileDTO {

    @Schema(description = "Unique identifier of the user profile", example = "1")
    private Long id;

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long userId;

    @Schema(description = "First name of the user", example = "John")
    private String firstName = "Does not filled by User";

    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName = "Does not filled by User";

    @Schema(description = "Phone number of the user", example = "1234567890")
    private String phoneNumber = "Does not updated by user";

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email = "Does not filled by User";

    @Schema(description = "Address of the user", example = "123 Main St")
    private String address = "Does not filled by User";

    @Schema(description = "Preferences of the user", example = "email")
    private String preferences = "Does not filled by User";

    @Schema(description = "Date of birth of the user", example = "1990-01-01")
    private String dateOfBirth = "Does not filled by User";

    @Schema(description = "Profile picture URL of the user", example = "/images/profile1.jpg")
    private String profilePicture = "Does not filled by User";

    @Schema(description = "Date of birth in Persian calendar", example = "1368-10-11")
    private String dateOfBirthPersian = "Does not filled by User";
}
