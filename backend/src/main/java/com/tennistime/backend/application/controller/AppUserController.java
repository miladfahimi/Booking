package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.service.AppUserService;
import com.tennistime.backend.application.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        List<AppUserDTO> users = appUserService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Long id) {
        AppUserDTO user = appUserService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTO appUserDTO) {
        AppUserDTO createdUser = appUserService.save(appUserDTO);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        appUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reservations")
    @Operation(summary = "Get reservations by user ID")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUserId(@PathVariable Long id) {
        List<ReservationDTO> reservations = reservationService.findReservationsByUserId(id);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up a new user")
    public ResponseEntity<AppUserDTO> signup(@RequestBody AppUserDTO appUserDTO) {
        AppUserDTO signedUpUser = appUserService.signup(appUserDTO);
        return ResponseEntity.ok(signedUpUser);
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in a user")
    public ResponseEntity<AppUserDTO> signin(@RequestParam String email, @RequestParam String password) {
        AppUserDTO signedInUser = appUserService.signin(email, password);
        return ResponseEntity.ok(signedInUser);
    }

    @GetMapping("/verify")
    @Operation(summary = "Verify user email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        appUserService.verifyEmail(token);
        return ResponseEntity.noContent().build();
    }
}
