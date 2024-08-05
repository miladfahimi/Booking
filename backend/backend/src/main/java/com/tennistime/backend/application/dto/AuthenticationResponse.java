package com.tennistime.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String token;
}
