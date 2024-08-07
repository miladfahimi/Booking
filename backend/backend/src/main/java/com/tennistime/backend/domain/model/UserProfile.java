package com.tennistime.backend.domain.model;

import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private String preferences;

    @Column(nullable = false)
    private boolean isUserProfilesInitiated = false; // New column

    @Transient
    private PersianDate dateOfBirthPersian;

    public PersianDate getDateOfBirthPersian() {
        return dateOfBirth != null ? PersianDate.fromGregorian(dateOfBirth) : null;
    }

    public void setDateOfBirthPersian(PersianDate persianDate) {
        this.dateOfBirth = persianDate != null ? persianDate.toGregorian() : null;
    }
}
