package com.tennistime.bff.infrastructure.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Configures client side STOMP infrastructure used to consume reservation updates.
 */
@Configuration
public class RealtimeClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(RealtimeClientConfig.class);

    /**
     * Creates a STOMP client capable of handling reconnection attempts.
     *
     * @param messageConverter converter used for payload serialization
     * @param taskScheduler    scheduler used for heartbeats and reconnect attempts
     * @return configured STOMP client instance
     */
    @Bean
    public WebSocketStompClient slotStatusStompClient(MappingJackson2MessageConverter messageConverter,
                                                      ThreadPoolTaskScheduler taskScheduler) {
        logger.info("\033[1;34m[Realtime]\033[0m Initializing WebSocketStompClient for slot status bridge");
        WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());
        client.setMessageConverter(messageConverter);
        client.setTaskScheduler(taskScheduler);
        client.setDefaultHeartbeat(new long[]{10000, 10000});
        return client;
    }

    /**
     * Supplies a Jackson based converter shared by the STOMP client.
     *
     * @param objectMapper configured object mapper
     * @return mapping converter instance
     */
    @Bean
    public MappingJackson2MessageConverter stompMessageConverter(ObjectMapper objectMapper) {
        logger.info("\033[1;34m[Realtime]\033[0m Initializing MappingJackson2MessageConverter for STOMP");
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    /**
     * Provides a scheduler to drive heartbeat and reconnection tasks.
     *
     * @return configured task scheduler
     */
    @Bean
    public ThreadPoolTaskScheduler stompTaskScheduler() {
        logger.info("\033[1;34m[Realtime]\033[0m Initializing ThreadPoolTaskScheduler for STOMP heartbeats");
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("slot-status-stomp-");
        return scheduler;
    }
}
