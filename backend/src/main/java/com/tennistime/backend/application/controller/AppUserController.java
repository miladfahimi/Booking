package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.service.AppUserService;
import com.tennistime.backend.application.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        List<AppUserDTO> users = appUserService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Long id) {
        AppUserDTO user = appUserService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTO appUserDTO) {
        AppUserDTO createdUser = appUserService.save(appUserDTO);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        appUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUserId(@PathVariable Long id) {
        List<ReservationDTO> reservations = reservationService.findReservationsByUserId(id);
        return ResponseEntity.ok(reservations);
    }
}
