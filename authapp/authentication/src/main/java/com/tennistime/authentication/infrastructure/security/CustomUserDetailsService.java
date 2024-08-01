package com.tennistime.authentication.infrastructure.security;

import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(emailOrPhone)
                .orElseGet(() -> userRepository.findByPhone(emailOrPhone)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email/phone: " + emailOrPhone)));

        // Logging
        System.out.println("\033[1;34m----------------------------\033[0m");
        System.out.println("\033[1;34m[CustomUserDetailsService] User loaded: " + user + "\033[0m");
        System.out.println("\033[1;34m----------------------------\033[0m");

        return new CustomUserDetails(user);
    }
}
