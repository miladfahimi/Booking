package com.tennistime.reservation.infrastructure.feign;

import com.tennistime.reservation.application.dto.UserBookingHistoryUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "profile-service", url = "http://localhost:8099/api/v1")
public interface UserBookingHistoryClient {

    @PostMapping("/user/history/update")
    void updateUserBookingHistory(UserBookingHistoryUpdateRequest request);
}
