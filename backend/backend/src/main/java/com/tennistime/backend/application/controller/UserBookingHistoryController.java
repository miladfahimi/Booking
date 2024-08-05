package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.service.UserBookingHistoryService;
import com.tennistime.backend.domain.model.UserBookingHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/history")
@RequiredArgsConstructor
public class UserBookingHistoryController {

    private final UserBookingHistoryService userBookingHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<UserBookingHistory> getUserBookingHistory(@PathVariable Long id) {
        Optional<UserBookingHistory> userBookingHistory = userBookingHistoryService.getUserBookingHistory(id);
        return userBookingHistory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserBookingHistory> createUserBookingHistory(@RequestBody UserBookingHistory userBookingHistory) {
        UserBookingHistory savedUserBookingHistory = userBookingHistoryService.createUserBookingHistory(userBookingHistory);
        return ResponseEntity.ok(savedUserBookingHistory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserBookingHistory> updateUserBookingHistory(@PathVariable Long id, @RequestBody UserBookingHistory updatedBookingHistory) {
        Optional<UserBookingHistory> userBookingHistory = userBookingHistoryService.updateUserBookingHistory(id, updatedBookingHistory);
        return userBookingHistory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserBookingHistory(@PathVariable Long id) {
        userBookingHistoryService.deleteUserBookingHistory(id);
        return ResponseEntity.ok().build();
    }
}
