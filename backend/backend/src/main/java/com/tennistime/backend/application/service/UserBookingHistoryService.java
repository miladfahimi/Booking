package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.userDetails.UserBookingHistoryDTO;
import com.tennistime.backend.application.mapper.UserBookingHistoryMapper;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.model.UserBookingHistory;
import com.tennistime.backend.domain.repository.CourtRepository;
import com.tennistime.backend.domain.repository.UserBookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service class for managing user booking history.
 */
@Service
@RequiredArgsConstructor
public class UserBookingHistoryService {

    private final UserBookingHistoryRepository userBookingHistoryRepository;
    private final CourtRepository courtRepository;
    private final UserBookingHistoryMapper userBookingHistoryMapper;

    /**
     * Retrieves a user booking history record by its ID.
     *
     * @param id the ID of the user booking history record
     * @return an Optional containing the UserBookingHistoryDTO if found, or empty if not found
     */
    public Optional<UserBookingHistoryDTO> getUserBookingHistory(Long id) {
        return userBookingHistoryRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Creates a new user booking history record.
     *
     * @param userBookingHistoryDTO the DTO containing the user booking history data
     * @return the created UserBookingHistoryDTO
     */
    public UserBookingHistoryDTO createUserBookingHistory(UserBookingHistoryDTO userBookingHistoryDTO) {
        UserBookingHistory userBookingHistory = convertToEntity(userBookingHistoryDTO);
        UserBookingHistory savedUserBookingHistory = userBookingHistoryRepository.save(userBookingHistory);
        return convertToDTO(savedUserBookingHistory);
    }

    /**
     * Updates an existing user booking history record.
     *
     * @param id the ID of the user booking history record to update
     * @param updatedBookingHistoryDTO the DTO containing the updated booking history data
     * @return an Optional containing the updated UserBookingHistoryDTO if the update was successful, or empty if not
     */
    public Optional<UserBookingHistoryDTO> updateUserBookingHistory(Long id, UserBookingHistoryDTO updatedBookingHistoryDTO) {
        return userBookingHistoryRepository.findById(id)
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

    /**
     * Deletes a user booking history record by its ID.
     *
     * @param id the ID of the user booking history record to delete
     */
    public void deleteUserBookingHistory(Long id) {
        userBookingHistoryRepository.deleteById(id);
    }

    /**
     * Converts a UserBookingHistory entity to a UserBookingHistoryDTO.
     *
     * @param userBookingHistory the UserBookingHistory entity to convert
     * @return the converted UserBookingHistoryDTO
     */
    private UserBookingHistoryDTO convertToDTO(UserBookingHistory userBookingHistory) {
        UserBookingHistoryDTO dto = userBookingHistoryMapper.toDTO(userBookingHistory);
        dto.setBookingDatePersian(userBookingHistory.getBookingDatePersian() != null ? userBookingHistory.getBookingDatePersian().toString() : "Does not filled by User");
        dto.setCourtId(userBookingHistory.getCourt().getId());
        dto.setBookingDate(userBookingHistory.getBookingDate().toString());
        return dto;
    }

    /**
     * Converts a UserBookingHistoryDTO to a UserBookingHistory entity.
     *
     * @param userBookingHistoryDTO the UserBookingHistoryDTO to convert
     * @return the converted UserBookingHistory entity
     */
    private UserBookingHistory convertToEntity(UserBookingHistoryDTO userBookingHistoryDTO) {
        UserBookingHistory userBookingHistory = userBookingHistoryMapper.toEntity(userBookingHistoryDTO);
        userBookingHistory.setBookingDate(LocalDateTime.parse(userBookingHistoryDTO.getBookingDate()));
        userBookingHistory.setBookingDatePersian(userBookingHistoryDTO.getBookingDatePersian() != null && !userBookingHistoryDTO.getBookingDatePersian().equals("Does not filled by User") ? PersianDate.parse(userBookingHistoryDTO.getBookingDatePersian()) : null);
        courtRepository.findById(userBookingHistoryDTO.getCourtId()).ifPresent(userBookingHistory::setCourt);
        return userBookingHistory;
    }
}
