package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.userDetails.UserBookingHistoryDTO;
import com.tennistime.backend.application.mapper.UserBookingHistoryMapper;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.model.UserBookingHistory;
import com.tennistime.backend.domain.repository.CourtRepository;
import com.tennistime.backend.domain.repository.UserBookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBookingHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(UserBookingHistoryService.class);

    private final UserBookingHistoryRepository userBookingHistoryRepository;
    private final CourtRepository courtRepository;
    private final UserBookingHistoryMapper userBookingHistoryMapper;

    public List<UserBookingHistoryDTO> getUserBookingHistoryByUserId(UUID userId) {
        logger.info("\033[0;34mFetching user booking history for user ID: {}\033[0m", userId);
        return userBookingHistoryRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserBookingHistoryDTO createUserBookingHistory(UserBookingHistoryDTO userBookingHistoryDTO) {
        logger.info("\033[0;32mCreating new user booking history: {}\033[0m", userBookingHistoryDTO);
        UserBookingHistory userBookingHistory = convertToEntity(userBookingHistoryDTO);
        UserBookingHistory savedUserBookingHistory = userBookingHistoryRepository.save(userBookingHistory);
        return convertToDTO(savedUserBookingHistory);
    }

    public Optional<UserBookingHistoryDTO> updateUserBookingHistory(UUID userId, UserBookingHistoryDTO updatedBookingHistoryDTO) {
        logger.info("\033[0;33mUpdating user booking history for user ID: {}\033[0m", userId);
        return userBookingHistoryRepository.findByUserId(userId).stream()
                .findFirst()  // Modify logic based on your requirement
                .map(existingBookingHistory -> {
                    Optional<Court> courtOptional = courtRepository.findById(updatedBookingHistoryDTO.getCourtId());
                    courtOptional.ifPresent(existingBookingHistory::setCourt);
                    existingBookingHistory.setBookingDate(LocalDateTime.parse(updatedBookingHistoryDTO.getBookingDate()));
                    existingBookingHistory.setStatus(updatedBookingHistoryDTO.getStatus());
                    existingBookingHistory.setBookingDatePersian(
                            updatedBookingHistoryDTO.getBookingDatePersian() != null && !updatedBookingHistoryDTO.getBookingDatePersian().equals("Does not filled by User")
                                    ? PersianDate.parse(updatedBookingHistoryDTO.getBookingDatePersian())
                                    : null
                    );
                    UserBookingHistory savedBookingHistory = userBookingHistoryRepository.save(existingBookingHistory);
                    return convertToDTO(savedBookingHistory);
                });
    }

    public void deleteUserBookingHistoryByUserId(UUID userId) {
        logger.info("\033[0;31mDeleting user booking history for user ID: {}\033[0m", userId);
        userBookingHistoryRepository.findByUserId(userId).forEach(userBookingHistory -> userBookingHistoryRepository.deleteById(userBookingHistory.getId()));
    }

    private UserBookingHistoryDTO convertToDTO(UserBookingHistory userBookingHistory) {
        UserBookingHistoryDTO dto = userBookingHistoryMapper.toDTO(userBookingHistory);
        dto.setBookingDatePersian(userBookingHistory.getBookingDatePersian() != null ? userBookingHistory.getBookingDatePersian().toString() : "Does not filled by User");
        dto.setCourtId(userBookingHistory.getCourt().getId());
        dto.setBookingDate(userBookingHistory.getBookingDate().toString());
        return dto;
    }

    private UserBookingHistory convertToEntity(UserBookingHistoryDTO userBookingHistoryDTO) {
        UserBookingHistory userBookingHistory = userBookingHistoryMapper.toEntity(userBookingHistoryDTO);
        userBookingHistory.setBookingDate(LocalDateTime.parse(userBookingHistoryDTO.getBookingDate()));
        userBookingHistory.setBookingDatePersian(userBookingHistoryDTO.getBookingDatePersian() != null && !userBookingHistoryDTO.getBookingDatePersian().equals("Does not filled by User") ? PersianDate.parse(userBookingHistoryDTO.getBookingDatePersian()) : null);
        courtRepository.findById(userBookingHistoryDTO.getCourtId()).ifPresent(userBookingHistory::setCourt);
        return userBookingHistory;
    }
}
