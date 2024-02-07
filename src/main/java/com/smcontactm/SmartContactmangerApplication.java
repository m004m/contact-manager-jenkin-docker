package com.smcontactm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartContactmangerApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(SmartContactmangerApplication.class);
	public static void main(String[] args) {
		LOGGER.info("Application started----------------------------------------->");
		SpringApplication.run(SmartContactmangerApplication.class, args);
	}

}
