package com.tennistime.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "Tennis Club 1")
    private String name;

    @Schema(example = "123 Main St")
    private String address;

    @Schema(example = "1234567890")
    private String phone;

    @Schema(example = "club1@example.com")
    private String email;

    @Schema(example = "Best club in town")
    private String description;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(hidden = true)
    private List<Court> courts;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(hidden = true)
    private List<Feedback> feedbacks;
}
