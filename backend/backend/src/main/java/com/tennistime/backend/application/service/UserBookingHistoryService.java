package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.UserBookingHistory;
import com.tennistime.backend.domain.repository.UserBookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBookingHistoryService {

    private final UserBookingHistoryRepository userBookingHistoryRepository;

    public Optional<UserBookingHistory> getUserBookingHistory(Long id) {
        return userBookingHistoryRepository.findById(id);
    }

    public UserBookingHistory createUserBookingHistory(UserBookingHistory userBookingHistory) {
        return userBookingHistoryRepository.save(userBookingHistory);
    }

    public Optional<UserBookingHistory> updateUserBookingHistory(Long id, UserBookingHistory updatedBookingHistory) {
        return userBookingHistoryRepository.findById(id).map(userBookingHistory -> {
            userBookingHistory.setCourt(updatedBookingHistory.getCourt());
            userBookingHistory.setBookingDate(updatedBookingHistory.getBookingDate());
            userBookingHistory.setStatus(updatedBookingHistory.getStatus());
            return userBookingHistoryRepository.save(userBookingHistory);
        });
    }

    public void deleteUserBookingHistory(Long id) {
        userBookingHistoryRepository.deleteById(id);
    }
}
