package com.tennistime.bff.interfaces.web.user;

import com.tennistime.bff.application.dto.AggregatedReservationDTO;
import com.tennistime.bff.application.service.ReservationAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/portal/user")
@RequiredArgsConstructor
public class UserPortalController {

    private final ReservationAggregationService reservationAggregationService;

    @GetMapping("/reservations/{reservationId}/details")
    public ResponseEntity<AggregatedReservationDTO> getAggregatedReservationDetails(@PathVariable UUID reservationId) {
        AggregatedReservationDTO aggregatedReservation = reservationAggregationService.getAggregatedReservation(reservationId);
        return ResponseEntity.ok(aggregatedReservation);
    }
}
