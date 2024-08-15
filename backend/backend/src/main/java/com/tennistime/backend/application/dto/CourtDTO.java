package com.tennistime.backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtDTO {
    @Schema(hidden = true)
    private UUID id;
    private String name;
    private String type;
    private boolean availability;
    private UUID providerId; // Assuming a court belongs to a provider
}
