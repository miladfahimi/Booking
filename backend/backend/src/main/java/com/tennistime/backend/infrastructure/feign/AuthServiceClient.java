package com.tennistime.backend.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "authentication-service", url = "http://localhost:8095/api/v1/service")
public interface AuthServiceClient {

    @GetMapping("/test")
    String testEndpoint();
}
