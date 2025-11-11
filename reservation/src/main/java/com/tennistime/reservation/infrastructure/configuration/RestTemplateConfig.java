package com.tennistime.reservation.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring configuration exposing a {@link RestTemplate} bean for HTTP integrations.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a default {@link RestTemplate} instance with the standard message converters.
     *
     * @return configured {@link RestTemplate} bean.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
