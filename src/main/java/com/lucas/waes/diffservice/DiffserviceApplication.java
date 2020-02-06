package com.lucas.waes.diffservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.service.DiffService;

@SpringBootApplication
public class DiffserviceApplication {
	
	public static void main(String[] args) throws DiffException {
		SpringApplication.run(DiffserviceApplication.class, args);
	}

}
