package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.ReservationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Feign client for interacting with the Reservation Service.
 * It includes endpoints for fetching reservations by various criteria.
 */
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

    @GetMapping("/reservations/filter")
    List<ReservationDTO> getReservationsByFilters(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID serviceId,
            @RequestParam(required = false) UUID providerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
            @RequestParam(required = false) String persianDate,
            @RequestParam(required = false) LocalTime startTime,
            @RequestParam(required = false) LocalTime endTime
    );

    @GetMapping("/reservations/test/public")
    String testReservationServiceFeignConnectivity();
}
