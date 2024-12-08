package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.ProviderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "provider-service", url = "http://backend:8081/api/v1")
public interface ProviderServiceClient {

    @GetMapping("/providers/{id}")
    ProviderDTO getProviderById(@PathVariable("id") UUID providerId);

    @GetMapping("/providers")
    List<ProviderDTO> getAllProviders();
}
