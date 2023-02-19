package com.nebrija.tfg.pruebas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.nebrija.tfg.pruebas"})
@EnableMongoRepositories
public class PruebasApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebasApplication.class, args);
	}

}
