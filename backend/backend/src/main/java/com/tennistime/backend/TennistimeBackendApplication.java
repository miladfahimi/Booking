package com.tennistime.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;


@SpringBootApplication
@EnableFeignClients
public class TennistimeBackendApplication implements CommandLineRunner {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(TennistimeBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\033[1;31m----------------------------\033[0m");
		System.out.println("\033[1;31mActive Profiles Backend: " + env.getActiveProfiles() + "\033[0m");
		System.out.println("\033[1;31m----------------------------\033[0m");
    }


}
