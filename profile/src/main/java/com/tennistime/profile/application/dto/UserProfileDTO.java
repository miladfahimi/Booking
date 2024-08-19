package com.tennistime.profile.application.dto;

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
    private UUID id;

    @Schema(description = "User ID", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID userId;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email = "Does not filled by User";

    @Schema(description = "First name", example = "John")
    private String firstName = "Does not filled by User";

    @Schema(description = "Last name", example = "Doe")
    private String lastName = "Does not filled by User";

    @Schema(description = "Phone number", example = "1234567890")
    private String phone = "Does not filled by User";

    @Schema(description = "Address", example = "123 Main St")
    private String address = "Does not filled by User";

    @Schema(description = "Gender", example = "Male")
    private String gender = "Does not filled by User";

    @Schema(description = "Date of birth", example = "1990-01-01")
    private String dateOfBirth;

    @Schema(description = "Profile picture URL", example = "/images/profile1.jpg")
    private String profilePicture = "Does not filled by User";

    @Schema(description = "User preferences", example = "email")
    private String preferences = "Does not filled by User";

    @Schema(description = "Date of birth in Persian calendar", example = "1368-10-11")
    private String dateOfBirthPersian = "Does not filled by User";
}
