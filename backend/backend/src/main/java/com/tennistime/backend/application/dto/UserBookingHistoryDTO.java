package com.tennistime.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBookingHistoryDTO {
    private Long id;
    private Long userId;
    private Long courtId;
    private LocalDateTime bookingDate;
    private String status;
    private String bookingDatePersian;
}
