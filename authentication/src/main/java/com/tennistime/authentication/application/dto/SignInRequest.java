package com.tennistime.authentication.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    private String email;
    private String password;
    private String deviceModel;
    private String os;
    private String browser;
}
