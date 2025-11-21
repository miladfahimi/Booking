package com.tennistime.reservation.infrastructure.configuration;

import com.tennistime.reservation.infrastructure.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@Profile({"dev", "docker"})
public class DevSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(DevSecurityConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public DevSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("\033[1;32m----------------------------\033[0m");
        logger.info("\033[1;32mConfiguring Security Filter Chain for Development Profile\033[0m");
        logger.info("\033[1;32m----------------------------\033[0m");

        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers(
                                "/h2-console/**",
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
                                "/cache/invalidate",
                                "/logout",
                                "/reservations/**",
                                "/ws/reservation/**",
                                "/ws/reservation",
                                "/api/v1/ws/reservation/**",
                                "/api/v1/ws/reservation"
                        ).permitAll()
                        .requestMatchers("/reservations/test/admin/**").hasRole("ADMIN")
                        .requestMatchers("/reservations/test/public/**").hasAnyRole("ADMIN","PROVIDER_OWNER","USER")
                        .requestMatchers("/reservations/**").hasAnyRole("ADMIN","PROVIDER_OWNER","USER")
                        .requestMatchers("/reservations/test/provider/**").hasRole("PROVIDER_OWNER")
                        .requestMatchers("/reservations/test/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions().sameOrigin());

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("http://localhost:4200");
        corsConfiguration.addAllowedOrigin("http://192.168.0.16:80");
        corsConfiguration.addAllowedOrigin("http://192.168.0.16:8083");
        corsConfiguration.addAllowedOrigin("http://bff:8083");
        corsConfiguration.addAllowedOrigin("http://bff");
        corsConfiguration.addAllowedOrigin("http://192.168.0.16");
        corsConfiguration.addAllowedOrigin("http://frontend:80");
        corsConfiguration.addAllowedOrigin("http://frontend");
        corsConfiguration.addAllowedOrigin("http://49.12.109.90");
        corsConfiguration.addAllowedOrigin("http://46.62.151.135:8082");
        corsConfiguration.addAllowedOriginPattern("*");

        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}
