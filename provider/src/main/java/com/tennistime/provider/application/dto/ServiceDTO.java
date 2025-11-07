package com.tennistime.provider.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    @Schema(hidden = true)
    private UUID id;

    @Schema(example = "Service 1")
    private String name;

    @Schema(example = "Clay")
    private String type;

    @Schema(example = "true")
    private boolean availability;

    @Schema(example = "b2a04e78-4d4a-4f54-bec5-3b7c1f4b2f24", description = "ID of the provider associated with this service")
    private UUID providerId;

    @Schema(example = "08:00", description = "The start time when the service is available")
    private LocalTime startTime;

    @Schema(example = "20:00", description = "The end time when the service is available")
    private LocalTime endTime;

    @Schema(example = "60", description = "Duration of each time slot in minutes")
    private int slotDuration;

    @Schema(example = "10", description = "Gap in minutes reserved between slots for preparation or maintenance")
    private Integer slotGapDuration;

    @Schema(example = "A high-quality clay tennis court", description = "Detailed description of the service")
    private String description;

    @Schema(example = "[\"beginner\", \"outdoor\"]", description = "Tags or categories for filtering and searching services")
    private List<String> tags;

    @Schema(example = "100.00", description = "The price of the service")
    private BigDecimal price;

    @Schema(example = "10", description = "The maximum capacity for the service")
    private int maxCapacity;
}
