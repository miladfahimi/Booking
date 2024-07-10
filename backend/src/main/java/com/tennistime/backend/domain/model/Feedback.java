package com.tennistime.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "Great club!")
    private String comment;

    @Schema(example = "5")
    private int rating;

    @Schema(example = "2024-07-04T19:51:35.830Z")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    @JsonBackReference(value = "club-feedbacks")
    @Schema(hidden = true)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id")
    @JsonBackReference(value = "court-feedbacks")
    @Schema(hidden = true)
    private Court court;
}
