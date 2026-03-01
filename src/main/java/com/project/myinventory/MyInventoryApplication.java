package com.project.myinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class MyInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyInventoryApplication.class, args);
	}

}
