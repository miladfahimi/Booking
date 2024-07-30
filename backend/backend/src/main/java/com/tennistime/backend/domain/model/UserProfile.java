package com.tennistime.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Storing user ID instead of user reference

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private String preferences;

    @Transient
    private PersianDate dateOfBirthPersian;

    public PersianDate getDateOfBirthPersian() {
        return PersianDate.fromGregorian(this.dateOfBirth);
    }

    public void setDateOfBirthPersian(PersianDate persianDate) {
        this.dateOfBirth = persianDate.toGregorian();
    }
}
