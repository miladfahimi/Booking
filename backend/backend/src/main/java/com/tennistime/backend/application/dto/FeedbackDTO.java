package com.tennistime.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private Long id;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
    private Long clubId;
    private Long courtId;
}
