package com.tennistime.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    private int rating;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    @JsonBackReference
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id")
    @JsonBackReference
    private Court court;

    // Constructors, getters, and setters

    public Feedback() {
    }

    public Feedback(String comment, int rating, Club club, Court court, LocalDateTime createdAt) {
        this.comment = comment;
        this.rating = rating;
        this.club = club;
        this.court = court;
        this.createdAt = createdAt;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
