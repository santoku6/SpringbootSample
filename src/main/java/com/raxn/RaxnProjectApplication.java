package com.raxn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RaxnProjectApplication {
	
	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/rechaxn");
		SpringApplication.run(RaxnProjectApplication.class, args);
	}

}
