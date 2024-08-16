package com.tennistime.profile.domain.model;

import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    private LocalDate dateOfBirth;

    @Transient
    private PersianDate dateOfBirthPersian;

    private String profilePictureUrl;
    private String preferences;
    private String gender;
    private String nationality;
    private String languagePreference;
    private String timezone;
    private String accountStatus;
    private Boolean emailVerified;
    private String profileVisibility;
    private Boolean notificationsEnabled;

    @Column(nullable = false)
    private boolean isUserProfilesInitiated = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();  // Update timestamp on modification
    }

    public PersianDate getDateOfBirthPersian() {
        return dateOfBirth != null ? PersianDate.fromGregorian(dateOfBirth) : null;
    }

    public void setDateOfBirthPersian(PersianDate persianDate) {
        this.dateOfBirth = persianDate != null ? persianDate.toGregorian() : null;
    }
}
