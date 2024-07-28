package com.tennistime.authentication.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser appUser;

    private LocalDateTime expiryDate;
    private boolean used;

    public VerificationToken() {}

    public VerificationToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
        this.expiryDate = LocalDateTime.now().plusMinutes(10); // Valid for 10 minutes
        this.used = false;
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
