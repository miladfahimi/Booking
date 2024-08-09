package com.tennistime.profile.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.profile.application.dto.UserBookingHistoryDTO;
import com.tennistime.profile.application.mapper.UserBookingHistoryMapper;
import com.tennistime.profile.domain.model.UserBookingHistory;
import com.tennistime.profile.domain.repository.UserBookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBookingHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(UserBookingHistoryService.class);

    private final UserBookingHistoryRepository userBookingHistoryRepository;
    private final UserBookingHistoryMapper userBookingHistoryMapper;

    public List<UserBookingHistoryDTO> getUserBookingHistoryByUserId(UUID userId) {
        logger.info("\033[1;36m----------------------------\033[0m");
        logger.info("\033[1;34m[UserBookingHistoryService][Fetch]: Fetching user booking history for user ID: {}\033[0m", userId);
        logger.info("\033[1;36m----------------------------\033[0m");

        return userBookingHistoryRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserBookingHistoryDTO createUserBookingHistory(UserBookingHistoryDTO userBookingHistoryDTO) {
        logger.info("\033[1;36m----------------------------\033[0m");
        logger.info("\033[1;35m[UserBookingHistoryService][Create]: Creating new user booking history: {}\033[0m", userBookingHistoryDTO);
        logger.info("\033[1;36m----------------------------\033[0m");

        UserBookingHistory userBookingHistory = convertToEntity(userBookingHistoryDTO);
        UserBookingHistory savedUserBookingHistory = userBookingHistoryRepository.save(userBookingHistory);

        logger.info("\033[1;32m[UserBookingHistoryService][Create]: Successfully created user booking history with ID: {}\033[0m", savedUserBookingHistory.getId());
        return convertToDTO(savedUserBookingHistory);
    }

    public Optional<UserBookingHistoryDTO> updateUserBookingHistory(UUID userId, UserBookingHistoryDTO updatedBookingHistoryDTO) {
        logger.info("\033[1;36m----------------------------\033[0m");
        logger.info("\033[1;34m[UserBookingHistoryService][Update]: Updating user booking history for user ID: {}\033[0m", userId);
        logger.info("\033[1;36m----------------------------\033[0m");

        return userBookingHistoryRepository.findByUserId(userId).stream()
                .findFirst()
                .map(existingBookingHistory -> {
                    try {
                        if (updatedBookingHistoryDTO.getBookingDate() != null && !updatedBookingHistoryDTO.getBookingDate().equals("Does not filled by User")) {
                            existingBookingHistory.setBookingDate(LocalDateTime.parse(updatedBookingHistoryDTO.getBookingDate()));
                        }
                    } catch (DateTimeParseException e) {
                        logger.warn("Invalid booking date format for user ID {}: {}", userId, updatedBookingHistoryDTO.getBookingDate());
                    }

                    existingBookingHistory.setStatus(updatedBookingHistoryDTO.getStatus());
                    existingBookingHistory.setItArchived(updatedBookingHistoryDTO.isItArchived());
                    existingBookingHistory.setUserNotified(updatedBookingHistoryDTO.isUserNotified());
                    existingBookingHistory.setNotes(updatedBookingHistoryDTO.getNotes());
                    existingBookingHistory.setReservationId(updatedBookingHistoryDTO.getReservationId());

                    if (updatedBookingHistoryDTO.getBookingDatePersian() != null && !updatedBookingHistoryDTO.getBookingDatePersian().equals("Does not filled by User")) {
                        existingBookingHistory.setBookingDatePersian(PersianDate.parse(updatedBookingHistoryDTO.getBookingDatePersian()));
                    }

                    UserBookingHistory savedBookingHistory = userBookingHistoryRepository.save(existingBookingHistory);

                    logger.info("\033[1;32m[UserBookingHistoryService][Update]: Successfully updated user booking history with ID: {}\033[0m", savedBookingHistory.getId());
                    return convertToDTO(savedBookingHistory);
                });
    }

    public void deleteUserBookingHistoryByUserId(UUID userId) {
        logger.info("\033[1;36m----------------------------\033[0m");
        logger.info("\033[1;31m[UserBookingHistoryService][Delete]: Deleting user booking history for user ID: {}\033[0m", userId);
        logger.info("\033[1;36m----------------------------\033[0m");

        userBookingHistoryRepository.findByUserId(userId).forEach(userBookingHistory -> userBookingHistoryRepository.deleteById(userBookingHistory.getId()));

        logger.info("\033[1;32m[UserBookingHistoryService][Delete]: Successfully deleted user booking history for user ID: {}\033[0m", userId);
    }

    private UserBookingHistoryDTO convertToDTO(UserBookingHistory userBookingHistory) {
        UserBookingHistoryDTO dto = userBookingHistoryMapper.toDTO(userBookingHistory);
        dto.setBookingDate(userBookingHistory.getBookingDate().toString());
        dto.setCreatedAt(userBookingHistory.getCreatedAt().toString());
        dto.setUpdatedAt(userBookingHistory.getUpdatedAt().toString());
        dto.setBookingDatePersian(userBookingHistory.getBookingDatePersian() != null ? userBookingHistory.getBookingDatePersian().toString() : null);
        return dto;
    }

    private UserBookingHistory convertToEntity(UserBookingHistoryDTO userBookingHistoryDTO) {
        UserBookingHistory userBookingHistory = userBookingHistoryMapper.toEntity(userBookingHistoryDTO);
        try {
            userBookingHistory.setBookingDate(LocalDateTime.parse(userBookingHistoryDTO.getBookingDate()));
        } catch (DateTimeParseException e) {
            logger.warn("Invalid booking date format in DTO: {}", userBookingHistoryDTO.getBookingDate());
            userBookingHistory.setBookingDate(null); // or set a default date
        }

        if (userBookingHistoryDTO.getBookingDatePersian() != null && !userBookingHistoryDTO.getBookingDatePersian().equals("Does not filled by User")) {
            userBookingHistory.setBookingDatePersian(PersianDate.parse(userBookingHistoryDTO.getBookingDatePersian()));
        }

        return userBookingHistory;
    }
}
