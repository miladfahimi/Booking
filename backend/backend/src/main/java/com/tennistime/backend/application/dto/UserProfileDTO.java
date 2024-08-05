package com.tennistime.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private Long userId;
    private String firstName = "Does not filled by User";
    private String lastName = "Does not filled by User";
    private String phoneNumber = "Does not updated by user";
    private String email = "Does not filled by User";
    private String address = "Does not filled by User";
    private String preferences = "Does not filled by User";
    private String dateOfBirth = "Does not filled by User";
    private String profilePicture = "Does not filled by User";
    private String dateOfBirthPersian = "Does not filled by User";
}
