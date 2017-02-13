package com.usermanagement.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.usermanagement"})
@EntityScan("com.usermanagement.model")
@EnableJpaRepositories("com.usermanagement.model")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

}