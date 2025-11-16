package com.tennistime.reservation.infrastructure.realtime;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures the STOMP broker responsible for delivering reservation slot status updates.
 */
@Configuration
@EnableWebSocketMessageBroker
public class ReservationWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /** {@inheritDoc} */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/reservation").setAllowedOriginPatterns("*");
    }

    /** {@inheritDoc} */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
