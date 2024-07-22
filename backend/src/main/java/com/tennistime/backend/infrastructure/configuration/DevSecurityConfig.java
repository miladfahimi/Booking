package com.tennistime.backend.infrastructure.configuration;

import com.tennistime.backend.infrastructure.security.CustomUserDetailsService;
import com.tennistime.backend.infrastructure.security.JwtAuthenticationEntryPoint;
import com.tennistime.backend.infrastructure.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("dev")
public class DevSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(DevSecurityConfig.class);

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("\033[1;32m----------------------------\033[0m");
        logger.info("\033[1;32mConfiguring Security Filter Chain for Development Profile\033[0m");
        logger.info("\033[1;32m----------------------------\033[0m");

        http
                .authorizeRequests(auth -> auth
                        .requestMatchers(
                                "/h2-console/**",
                                "/api/v1/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/users/signup",  // Allow signup without JWT
                                "/users/signin"   // Allow signin without JWT
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Allow frames from the same origin
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF for H2 console
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Add the JWT authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("\033[1;32m----------------------------\033[0m");
        logger.info("\033[1;32mSecurity Filter Chain configured successfully for Development Profile\033[0m");
        logger.info("\033[1;32m----------------------------\033[0m");

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        logger.info("\033[1;33m----------------------------\033[0m");
        logger.info("\033[1;33mConfiguring Authentication Manager\033[0m");
        logger.info("\033[1;33m----------------------------\033[0m");

        AuthenticationManager authManager = http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();

        logger.info("\033[1;33m----------------------------\033[0m");
        logger.info("\033[1;33mAuthentication Manager configured successfully\033[0m");
        logger.info("\033[1;33m----------------------------\033[0m");

        return authManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("\033[1;34m----------------------------\033[0m");
        logger.info("\033[1;34mCreating Password Encoder Bean\033[0m");
        logger.info("\033[1;34m----------------------------\033[0m");

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        logger.info("\033[1;34m----------------------------\033[0m");
        logger.info("\033[1;34mPassword Encoder Bean created successfully\033[0m");
        logger.info("\033[1;34m----------------------------\033[0m");

        return encoder;
    }
}
