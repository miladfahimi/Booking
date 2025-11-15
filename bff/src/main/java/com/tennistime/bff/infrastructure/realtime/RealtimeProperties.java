package com.tennistime.bff.infrastructure.realtime;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties describing the realtime topology between services.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "realtime")
public class RealtimeProperties {

    private Reservation reservation = new Reservation();
    private Bff bff = new Bff();

    @Getter
    @Setter
    public static class Reservation {
        private String url;
        private String topic;
    }

    @Getter
    @Setter
    public static class Bff {
        private String topic;
    }
}
