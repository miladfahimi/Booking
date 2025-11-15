package com.tennistime.bff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import com.tennistime.bff.infrastructure.realtime.RealtimeProperties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableConfigurationProperties(RealtimeProperties.class)
public class TennistimeBffApplication implements CommandLineRunner {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(TennistimeBffApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String activeProfiles = String.join(", ", env.getActiveProfiles());
		System.out.println("\033[1;31m----------------------------\033[0m");
		System.out.println("\033[1;31mActive Profiles: " + (activeProfiles.isEmpty() ? "default" : activeProfiles) + "\033[0m");
		System.out.println("\033[1;31mApplication started on port: " + env.getProperty("server.port") + "\033[0m");
		System.out.println("\033[1;31m----------------------------\033[0m");
	}
}