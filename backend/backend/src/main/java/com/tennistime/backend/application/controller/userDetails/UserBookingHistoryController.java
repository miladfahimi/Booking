package com.tennistime.backend.application.controller.userDetails;

import com.tennistime.backend.application.dto.userDetails.UserBookingHistoryDTO;
import com.tennistime.backend.application.service.UserBookingHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/user/history")
@Tag(name = "User Booking History", description = "Manage user booking history")
@RequiredArgsConstructor
@Validated
public class UserBookingHistoryController {

    private final UserBookingHistoryService userBookingHistoryService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user booking history by ID", description = "Retrieve a specific booking history record by its ID.")
    public ResponseEntity<UserBookingHistoryDTO> getUserBookingHistory(@PathVariable Long id) {
        Optional<UserBookingHistoryDTO> userBookingHistory = userBookingHistoryService.getUserBookingHistory(id);
        return userBookingHistory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user booking history", description = "Allows users to create a new booking history record.")
    public ResponseEntity<UserBookingHistoryDTO> createUserBookingHistory(@Valid @RequestBody UserBookingHistoryDTO userBookingHistoryDTO) {
        UserBookingHistoryDTO savedUserBookingHistory = userBookingHistoryService.createUserBookingHistory(userBookingHistoryDTO);
        return new ResponseEntity<>(savedUserBookingHistory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user booking history by ID", description = "Allows users to update an existing booking history record.")
    public ResponseEntity<UserBookingHistoryDTO> updateUserBookingHistory(@PathVariable Long id, @Valid @RequestBody UserBookingHistoryDTO updatedBookingHistoryDTO) {
        Optional<UserBookingHistoryDTO> userBookingHistory = userBookingHistoryService.updateUserBookingHistory(id, updatedBookingHistoryDTO);
        return userBookingHistory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user booking history by ID", description = "Allows users to delete a specific booking history record.")
    public ResponseEntity<Void> deleteUserBookingHistory(@PathVariable Long id) {
        userBookingHistoryService.deleteUserBookingHistory(id);
        return ResponseEntity.ok().build();
    }
}
