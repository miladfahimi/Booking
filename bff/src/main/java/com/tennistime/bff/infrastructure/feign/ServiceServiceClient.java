package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "service-service", url = "http://localhost:8090/api/v1")
public interface ServiceServiceClient {

    @GetMapping("/services/{id}")
    ServiceDTO getServiceById(@PathVariable("id") UUID serviceId);
}
