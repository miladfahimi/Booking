package com.tennistime.authentication.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Schema(hidden = true)
    private UUID id;  // Changed from Long to UUID
    @Schema(example = "me")
    private String username;
    @Schema(example = "me@mail.com")
    private String email;
    @Schema(example = "1234567")
    private String phone;
    @NotEmpty(message = "Password cannot be empty")
    @Schema(example = "123")
    private String password;

    @Schema(example = "USER")
    private String role;

    @Schema(hidden = true)
    private String token;
    @Schema(hidden = true)
    private String deviceModel;
    @Schema(hidden = true)
    private String os;
    @Schema(hidden = true)
    private String browser;
}
