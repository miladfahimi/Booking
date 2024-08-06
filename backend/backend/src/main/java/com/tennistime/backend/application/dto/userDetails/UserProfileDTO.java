package com.tennistime.backend.application.dto.userDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User profile data transfer object")
public class UserProfileDTO {
    @Schema(description = "ID of the user profile", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    private UUID userId;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Phone number", example = "1234567890")
    private String phoneNumber;

    @Schema(description = "Address", example = "123 Main St")
    private String address;

    @Schema(description = "Date of birth", example = "1990-01-01")
    private String dateOfBirth;

    @Schema(description = "Profile picture URL", example = "/images/profile1.jpg")
    private String profilePicture;

    @Schema(description = "User preferences", example = "email")
    private String preferences;

    @Schema(description = "Date of birth in Persian calendar", example = "1368-10-11")
    private String dateOfBirthPersian;
}
