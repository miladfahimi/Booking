package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        List<AppUserDTO> users = appUserService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTO appUserDTO) {
        AppUserDTO createdUser = appUserService.save(appUserDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Long id) {
        AppUserDTO user = appUserService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        appUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUserId(@PathVariable Long id) {
        List<ReservationDTO> reservations = appUserService.findReservationsByUserId(id);
        return reservations != null ? ResponseEntity.ok(reservations) : ResponseEntity.notFound().build();
    }
}
