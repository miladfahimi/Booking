package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.ServiceDTO;
import com.tennistime.bff.application.dto.UserProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "profile-service", url = "http://localhost:8099/api/v1")
public interface UserProfileServiceClient {

    @GetMapping("/user/profiles/{id}")
    UserProfileDTO getUserProfileById(@PathVariable("id") UUID profileId);
}
