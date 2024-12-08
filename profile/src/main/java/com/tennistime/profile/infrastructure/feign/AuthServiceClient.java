package com.tennistime.profile.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "authentication-service", url = "http://authentication:8082/api/v1")
public interface AuthServiceClient {

    @GetMapping("/test/feign")
    String testEndpoint();

    @PostMapping("/token/validate")
    Map<String, Object> validateToken(@RequestBody Map<String, String> request);
}
