package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.FeedbackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "feedback-service", url = "http://localhost:8090/api/v1")
public interface FeedbackServiceClient {

    @GetMapping("/feedbacks/provider/{providerId}")
    List<FeedbackDTO> getFeedbacksByProviderId(@PathVariable("providerId") UUID providerId);

    @GetMapping("/feedbacks/service/{serviceId}")
    List<FeedbackDTO> getFeedbacksByServiceId(@PathVariable("serviceId") UUID serviceId);

    @GetMapping("/feedbacks/service/{serviceId}/top-rated")
    List<FeedbackDTO> getTopRatedFeedbacksByService(@PathVariable("serviceId") UUID serviceId);

    @GetMapping("/feedbacks/provider/{providerId}/top-rated")
    List<FeedbackDTO> getTopRatedFeedbacksByProvider(@PathVariable("providerId") UUID providerId);
}
