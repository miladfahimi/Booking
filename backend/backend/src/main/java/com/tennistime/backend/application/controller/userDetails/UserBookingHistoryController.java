package com.tennistime.backend.application.controller.userDetails;

import com.tennistime.backend.application.dto.userDetails.UserBookingHistoryDTO;
import com.tennistime.backend.application.service.UserBookingHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/history")
@Tag(name = "User Booking History", description = "Manage user booking history")
@RequiredArgsConstructor
public class UserBookingHistoryController {

    private final UserBookingHistoryService userBookingHistoryService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user booking history", description = "Retrieve booking history for a given user ID")
    public ResponseEntity<UserBookingHistoryDTO> getUserBookingHistory(@PathVariable Long id) {
        Optional<UserBookingHistoryDTO> userBookingHistory = userBookingHistoryService.getUserBookingHistory(id);
        return userBookingHistory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user booking history", description = "Create a new booking history record for a user")
    public ResponseEntity<UserBookingHistoryDTO> createUserBookingHistory(@RequestBody UserBookingHistoryDTO userBookingHistoryDTO) {
        UserBookingHistoryDTO savedUserBookingHistory = userBookingHistoryService.createUserBookingHistory(userBookingHistoryDTO);
        return ResponseEntity.ok(savedUserBookingHistory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user booking history", description = "Update an existing booking history record for a user")
    public ResponseEntity<UserBookingHistoryDTO> updateUserBookingHistory(@PathVariable Long id, @RequestBody UserBookingHistoryDTO updatedBookingHistoryDTO) {
        Optional<UserBookingHistoryDTO> userBookingHistory = userBookingHistoryService.updateUserBookingHistory(id, updatedBookingHistoryDTO);
        return userBookingHistory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user booking history", description = "Delete a booking history record for a user")
    public ResponseEntity<Void> deleteUserBookingHistory(@PathVariable Long id) {
        userBookingHistoryService.deleteUserBookingHistory(id);
        return ResponseEntity.ok().build();
    }
}
