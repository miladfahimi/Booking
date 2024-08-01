package com.tennistime.authentication.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Schema(hidden = true)
    private Long id;
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
    private String token; // Add this field to store JWT token
}
