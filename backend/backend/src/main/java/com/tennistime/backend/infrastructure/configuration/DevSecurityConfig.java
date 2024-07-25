package com.tennistime.backend.infrastructure.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @Profile("dev")
    public class DevSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(DevSecurityConfig.class);


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("\033[1;32m----------------------------\033[0m");
        logger.info("\033[1;32mConfiguring Security Filter Chain for Development Profile\033[0m");
        logger.info("\033[1;32m----------------------------\033[0m");

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Allow access to H2 console
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Allow frames from the same origin
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for H2 console
        return http.build();


    }
}
