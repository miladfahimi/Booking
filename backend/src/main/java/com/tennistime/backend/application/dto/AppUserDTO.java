package com.tennistime.backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {

    @Schema(hidden = true)
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;

    @NotEmpty(message = "Password cannot be empty")
    @Schema(hidden = true)
    private String password;

    private String role;
}
