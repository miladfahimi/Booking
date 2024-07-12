package com.tennistime.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtDTO {
    private Long id;
    private String name;
    private String type;
    private boolean availability;
    private Long clubId; // Assuming a court belongs to a club
}
