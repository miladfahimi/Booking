package com.tennistime.bff.infrastructure.feign;

import com.tennistime.bff.application.dto.ReservationDTO;
import com.tennistime.bff.application.dto.ReservationBasketItemDTO;
import com.tennistime.bff.domain.model.types.ReservationStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Feign client for interacting with the Reservation Service.
 * It includes endpoints for fetching reservations by various criteria.
 */
@FeignClient(name = "reservation-service", url = "http://reservation:8085/api/v1")
public interface ReservationServiceClient {

    @GetMapping("/reservations/{id}")
    ReservationDTO getReservationById(@PathVariable("id") UUID reservationId);

    @GetMapping("/reservations/all")
    List<ReservationDTO> getAllReservations();

    @PostMapping("/reservations")
    ReservationDTO createReservation(@RequestBody ReservationDTO reservationDTO);

    @PostMapping("/reservations/bulk")
    List<ReservationDTO> createReservations(@RequestBody List<ReservationDTO> reservations);

    @GetMapping("/reservations/basket/{userId}")
    List<ReservationBasketItemDTO> getBasketItems(@PathVariable("userId") UUID userId);

    @PostMapping("/reservations/basket")
    ReservationBasketItemDTO addBasketItem(@RequestBody ReservationBasketItemDTO basketItemDTO);

    @GetMapping("/reservations/basket/service/{serviceId}")
    List<ReservationBasketItemDTO> getBasketItemsByServiceAndDate(
            @PathVariable("serviceId") UUID serviceId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate
    );

    @DeleteMapping("/reservations/basket/{userId}/{slotId}")
    void removeBasketItem(@PathVariable("userId") UUID userId, @PathVariable("slotId") String slotId);

    @DeleteMapping("/reservations/basket/{userId}")
    void clearBasket(@PathVariable("userId") UUID userId);

    @PutMapping("/reservations/basket/{userId}/{slotId}/status")
    ReservationBasketItemDTO updateBasketStatus(
            @PathVariable("userId") UUID userId,
            @PathVariable("slotId") String slotId,
            @RequestParam("status") ReservationStatus status
    );

    @PutMapping("/reservations/{id}/status")
    ReservationDTO updateReservationStatus(
            @PathVariable("id") UUID reservationId,
            @RequestParam("status") ReservationStatus status
    );

    @GetMapping("/reservations/service/{serviceId}")
    List<ReservationDTO> getReservationsByServiceId(@PathVariable("serviceId") UUID serviceId);

    @GetMapping("/reservations/user/{userId}")
    List<ReservationDTO> getReservationsByUserId(@PathVariable("userId") UUID userId);

    @GetMapping("/reservations/filter")
    List<ReservationDTO> getReservationsByFilters(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID serviceId,
            @RequestParam(required = false) UUID providerId,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
            @RequestParam(required = false) String persianDate,
            @RequestParam(required = false) LocalTime startTime,
            @RequestParam(required = false) LocalTime endTime
    );

    @GetMapping("/reservations/test/public")
    String testReservationServiceFeignConnectivity();
}