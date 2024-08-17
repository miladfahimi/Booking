package com.tennistime.backend.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tennistime.backend.infrastructure.feign.AuthServiceClient;

@Service
public class AuthService {

    @Autowired
    private AuthServiceClient authServiceClient;

    public String callTestEndpoint() {
        return authServiceClient.testEndpoint();
    }
}
