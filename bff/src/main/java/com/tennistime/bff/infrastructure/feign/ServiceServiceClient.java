package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "service-service", url = "http://backend:8081/api/v1")
public interface ServiceServiceClient {

    @GetMapping("/services/{id}")
    ServiceDTO getServiceById(@PathVariable("id") UUID serviceId);

    @GetMapping("/services/provider/{providerId}")
    List<ServiceDTO> getServicesByProviderId(@PathVariable("providerId") UUID providerId);
}
