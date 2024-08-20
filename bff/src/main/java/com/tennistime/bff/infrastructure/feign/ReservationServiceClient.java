package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.ReservationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "reservation-service", url = "http://localhost:8085/api/v1")
public interface ReservationServiceClient {

    @GetMapping("/reservations/{id}")
    ReservationDTO getReservationById(@PathVariable("id") UUID reservationId);

    @GetMapping("/reservations")
    List<ReservationDTO> getAllReservations();

    @GetMapping("/reservations/service/{serviceId}")
    List<ReservationDTO> getReservationsByServiceId(@PathVariable("serviceId") UUID serviceId);

    @GetMapping("/reservations/user/{userId}")
    List<ReservationDTO> getReservationsByUserId(@PathVariable("userId") UUID userId);

    @GetMapping("/reservations/test/public")
    String testReservationServiceFeignConnectivity();
}
