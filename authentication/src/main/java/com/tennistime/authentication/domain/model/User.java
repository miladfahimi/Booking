package com.tennistime.authentication.domain.model;

import com.tennistime.authentication.application.util.PersianDateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String role; // "ADMIN", "PROVIDER_OWNER", "USER"
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    private String status;
    private LocalDateTime updatedAt;
    private Boolean accountNonLocked = true;
    private Boolean accountNonExpired = true;
    private Boolean credentialsNonExpired = true;
    private Boolean enabled = true;
    private Integer failedLoginAttempts;
    private LocalDateTime lockoutExpirationTime;
    private LocalDateTime passwordChangedAt;
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpirationTime;
    private Boolean twoFactorEnabled;
    private String twoFactorSecret;
    private Boolean emailVerified;
    private String emailVerificationToken;
    private LocalDateTime emailVerificationTokenExpirationTime;
    private LocalDateTime lastPasswordChangeAt;

    // tracking sign-in and sign-up details
    private String lastLoginIp;
    private String lastLoginDeviceModel;
    private String lastLoginOS;
    private String lastLoginBrowser;

    private String signUpIp;
    private String signUpDeviceModel;
    private String signUpOS;
    private String signUpBrowser;

    // OTP fields
    private String otp;
    private LocalDateTime otpExpirationTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Transient
    private String jalaliCreatedAt;

    public String getJalaliCreatedAt() {
        return PersianDateUtil.localDateToString(this.createdAt.toLocalDate());
    }

    public void setJalaliCreatedAt(String jalaliCreatedAt) {
        this.createdAt = PersianDateUtil.stringToLocalDate(jalaliCreatedAt).atStartOfDay();
    }

    public void updateLoginInfo(String ip, String deviceModel, String os, String browser) {
        this.lastLoginIp = ip;
        this.lastLoginDeviceModel = deviceModel;
        this.lastLoginOS = os;
        this.lastLoginBrowser = browser;
        this.lastLogin = LocalDateTime.now();
    }

    public void updateSignUpInfo(String ip, String deviceModel, String os, String browser) {
        this.signUpIp = ip;
        this.signUpDeviceModel = deviceModel;
        this.signUpOS = os;
        this.signUpBrowser = browser;
        this.createdAt = LocalDateTime.now();
    }
}
