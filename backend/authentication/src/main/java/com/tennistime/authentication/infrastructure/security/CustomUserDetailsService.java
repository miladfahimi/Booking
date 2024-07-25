package com.tennistime.authentication.infrastructure.security;

import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Logging
        System.out.println("\033[1;34m----------------------------\033[0m");
        System.out.println("\033[1;34m[CustomUserDetailsService] User loaded: " + appUser + "\033[0m");
        System.out.println("\033[1;34m----------------------------\033[0m");

        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), appUser.getAuthorities());
    }
}
