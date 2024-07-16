package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.findById(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.save(reservationDTO);
        return ResponseEntity.ok(createdReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDTO>> findReservationsByUserId(@PathVariable Long userId) {
        List<ReservationDTO> reservations = reservationService.findReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/court/{courtId}")
    public ResponseEntity<List<ReservationDTO>> findReservationsByCourtId(@PathVariable Long courtId) {
        List<ReservationDTO> reservations = reservationService.findReservationsByCourtId(courtId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/persian/{date}")
    public ResponseEntity<List<ReservationDTO>> findReservationsByPersianDate(@PathVariable String date) {
        List<ReservationDTO> reservations = reservationService.findReservationsByPersianDate(date);
        return ResponseEntity.ok(reservations);
    }
}
