package com.lucas.waes.diffservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lucas.waes.diffservice.exception.DiffException;

@SpringBootApplication
public class DiffServiceApplication {
	
	public static void main(String[] args) throws DiffException {
		SpringApplication.run(DiffServiceApplication.class, args);
	}

}
