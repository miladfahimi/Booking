package com.tennistime.authentication;

import com.tennistime.authentication.application.service.VerificationInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication(scanBasePackages = {"com.tennistime.authentication", "com.tennistime.common"})
public class TennistimeAuthenticationApplication implements CommandLineRunner {

    @Autowired
    private VerificationInitializationService verificationInitializationService;

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public static void main(String[] args) {
        SpringApplication.run(TennistimeAuthenticationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\033[1;31m----------------------------\033[0m");
        System.out.println("\033[1;31mActive Profiles Authentication: " + String.join(", ", env.getActiveProfiles()) + "\033[0m");
        System.out.println("\033[1;31m----------------------------\033[0m");

        checkDatabaseConnectivity();
        checkRedisConnectivity();

        verificationInitializationService.initializeVerificationTokens();
    }

    private void checkDatabaseConnectivity() {
        System.out.println("\033[1;34mChecking Database Connectivity...\033[0m");
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("\033[1;32mDatabase Connection Successful!\033[0m");
            } else {
                System.out.println("\033[1;31mDatabase Connection Failed!\033[0m");
            }
        } catch (Exception e) {
            System.out.println("\033[1;31mDatabase Connection Error: " + e.getMessage() + "\033[0m");
        }
        System.out.println("\033[1;34m----------------------------\033[0m");
    }

    private void checkRedisConnectivity() {
        System.out.println("\033[1;34mChecking Redis Connectivity...\033[0m");
        try {
            redisConnectionFactory.getConnection().ping();
            System.out.println("\033[1;32mRedis Connection Successful!\033[0m");
        } catch (Exception e) {
            System.out.println("\033[1;31mRedis Connection Error: " + e.getMessage() + "\033[0m");
        }
        System.out.println("\033[1;34m----------------------------\033[0m");
    }
}