package com.tennistime.profile.application.controller;


import com.tennistime.profile.application.dto.UserBookingHistoryDTO;
import com.tennistime.profile.application.service.UserBookingHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user/history")
@Tag(name = "User Booking History", description = "Manage user booking history")
@RequiredArgsConstructor
@Validated
public class UserBookingHistoryController {

    private final UserBookingHistoryService userBookingHistoryService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user booking history by user ID", description = "Retrieve booking history records by user ID.")
    public ResponseEntity<List<UserBookingHistoryDTO>> getUserBookingHistory(@PathVariable UUID userId) {
        List<UserBookingHistoryDTO> userBookingHistory = userBookingHistoryService.getUserBookingHistoryByUserId(userId);
        if (userBookingHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userBookingHistory);
        }
    }

    @PostMapping
    @Operation(summary = "Create user booking history", description = "Allows users to create a new booking history record.")
    public ResponseEntity<UserBookingHistoryDTO> createUserBookingHistory(@Valid @RequestBody UserBookingHistoryDTO userBookingHistoryDTO) {
        UserBookingHistoryDTO savedUserBookingHistory = userBookingHistoryService.createUserBookingHistory(userBookingHistoryDTO);
        return new ResponseEntity<>(savedUserBookingHistory, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user booking history by user ID", description = "Allows users to update an existing booking history record.")
    public ResponseEntity<UserBookingHistoryDTO> updateUserBookingHistory(@PathVariable UUID userId, @Valid @RequestBody UserBookingHistoryDTO updatedBookingHistoryDTO) {
        Optional<UserBookingHistoryDTO> userBookingHistory = userBookingHistoryService.updateUserBookingHistory(userId, updatedBookingHistoryDTO);
        return userBookingHistory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user booking history by user ID", description = "Allows users to delete a specific booking history record.")
    public ResponseEntity<Void> deleteUserBookingHistory(@PathVariable UUID userId) {
        userBookingHistoryService.deleteUserBookingHistoryByUserId(userId);
        return ResponseEntity.ok().build();
    }
}
