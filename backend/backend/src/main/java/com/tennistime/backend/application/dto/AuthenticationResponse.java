package com.tennistime.backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    @Schema(description = "Unique identifier of the user", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @Schema(description = "Username of the user", example = "john.doe")
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number of the user", example = "1234567890")
    private String phone;

    @Schema(description = "Role of the user", example = "USER")
    private String role;

    @Schema(description = "Authentication token", example = "eyJhbGciOiJIUzUxMiJ9.eyJ...")
    private String token;
}
