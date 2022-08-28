package com.sparta.spring_team;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringTeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTeamApplication.class, args);
	}

}
