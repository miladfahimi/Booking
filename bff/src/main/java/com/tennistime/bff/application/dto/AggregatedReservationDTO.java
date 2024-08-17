package com.tennistime.bff.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedReservationDTO {
    private UUID reservationId;
    private LocalDate reservationDate;
    private String reservationDatePersian;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private UserProfileDTO userId;

    private ProviderDTO provider;
    private ServiceDTO service;
    private List<FeedbackDTO> serviceFeedbacks;
    private List<FeedbackDTO> providerFeedbacks;
}
