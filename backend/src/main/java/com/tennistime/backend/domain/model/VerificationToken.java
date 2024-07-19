package com.tennistime.backend.domain.model;

import jakarta.persistence.Id;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser appUser;

    private LocalDateTime expiryDate;

    public VerificationToken() {}

    public VerificationToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
        this.expiryDate = LocalDateTime.now().plusDays(1);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
