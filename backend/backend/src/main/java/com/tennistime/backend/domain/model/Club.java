package com.tennistime.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "club")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Court> courts = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Feedback> feedbacks = new ArrayList<>();

    public void addCourt(Court court) {
        courts.add(court);
        court.setClub(this);
    }

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        feedback.setClub(this);
    }
}
