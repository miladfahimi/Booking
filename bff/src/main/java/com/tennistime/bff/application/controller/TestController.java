package com.tennistime.bff.application.controller;

import com.tennistime.bff.application.service.ReservationAggregationService;
import com.tennistime.bff.infrastructure.feign.ReservationServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal/test")
public class TestController {

    @Autowired
    private ReservationAggregationService reservationAggregationService;

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Public Endpoint");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Admin Endpoint");
    }

    @GetMapping("/provider")
    public ResponseEntity<String> providerOwnerEndpoint() {
        return ResponseEntity.ok("provider Owner Endpoint");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userEndpoint() {
        return ResponseEntity.ok("User Endpoint");
    }

    @GetMapping("/feign")
    public ResponseEntity<String> testReservationServiceFeign() {
        String response = reservationAggregationService.testReservationServiceFeignConnectivity();
        return ResponseEntity.ok(response);
    }
}
