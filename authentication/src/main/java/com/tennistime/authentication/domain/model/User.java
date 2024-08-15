package com.tennistime.authentication.domain.model;

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
    private UUID id;  // Changed from Long to UUID
    private String username;
    private String email;
    private String phone;
    private String password;
    private String role; // "ADMIN", "PROVIDER_OWNER", "CLIENT"
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    // Additional fields for tracking sign-in and sign-up details
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
        return this.username; // use email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Method to update login information
    public void updateLoginInfo(String ip, String deviceModel, String os, String browser) {
        this.lastLoginIp = ip;
        this.lastLoginDeviceModel = deviceModel;
        this.lastLoginOS = os;
        this.lastLoginBrowser = browser;
        this.lastLogin = LocalDateTime.now();
    }

    // Method to update sign-up information
    public void updateSignUpInfo(String ip, String deviceModel, String os, String browser) {
        this.signUpIp = ip;
        this.signUpDeviceModel = deviceModel;
        this.signUpOS = os;
        this.signUpBrowser = browser;
        this.createdAt = LocalDateTime.now();
    }
}
