package com.tennistime.profile.application.service;

import com.tennistime.profile.infrastructure.feign.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthServiceClient authServiceClient;

    public String callTestEndpoint() {
        return authServiceClient.testEndpoint();
    }
}
