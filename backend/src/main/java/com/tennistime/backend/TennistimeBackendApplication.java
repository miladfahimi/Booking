package com.tennistime.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class TennistimeBackendApplication implements CommandLineRunner {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(TennistimeBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Active Profiles: " + String.join(", ", env.getActiveProfiles()));
	}
}
