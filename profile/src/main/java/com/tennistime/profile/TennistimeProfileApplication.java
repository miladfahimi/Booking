package com.tennistime.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@EnableFeignClients
public class TennistimeProfileApplication implements CommandLineRunner {

    @Autowired
    private Environment env;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(TennistimeProfileApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\033[1;31m----------------------------\033[0m");
        System.out.println("\033[1;31mActive Profiles << Profile service >>: " + String.join(", ", env.getActiveProfiles()) + "\033[0m");
        System.out.println("\033[1;31m----------------------------\033[0m");

        checkRedisConnection();
        checkPostgresConnection();

        System.out.println("\033[1;31m------------------------------------------------------\033[0m");
        System.out.println("\033[1;31m------------------------------------------------------\033[0m");
        System.out.println("\033[1;31m------------------------------------------------------\033[0m");
        System.out.println("\033[1;31m------------------------------------------------------\033[0m");
        System.out.println("\033[1;31m------------------------------------------------------\033[0m");

    }

    private void checkRedisConnection() {
        System.out.println("\033[1;33mChecking Redis connection...\033[0m");
        System.out.println("Redis Host: " + env.getProperty("spring.redis.host"));
        System.out.println("Redis Port: " + env.getProperty("spring.redis.port"));

        try {
            redisTemplate.opsForValue().set("testKey", "testValue");
            String value = redisTemplate.opsForValue().get("testKey");
            System.out.println("\033[1;32mRedis Connection Successful: testKey = " + value + "\033[0m");
        } catch (Exception e) {
            System.err.println("\033[1;31mRedis Connection Failed: " + e.getMessage() + "\033[0m");
            e.printStackTrace();
        }
    }


    private void checkPostgresConnection() {
        System.out.println("\033[1;33mChecking PostgreSQL connection...\033[0m");
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                System.out.println("\033[1;32mPostgreSQL Connection Successful: URL = " + connection.getMetaData().getURL() + "\033[0m");
            } else {
                System.err.println("\033[1;31mPostgreSQL Connection Failed: Connection is not valid\033[0m");
            }
        } catch (SQLException e) {
            System.err.println("\033[1;31mPostgreSQL Connection Failed: " + e.getMessage() + "\033[0m");
            e.printStackTrace();
        }
    }
}
