package com.hustlerdev.aiwebsitebuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class AiwebsitebuilderApplication {

	public static void main(String[] args) {
		// Force UTC timezone before Spring/Hibernate initializes.
		// Prevents "invalid value for parameter TimeZone: Asia/Calcutta" from PostgreSQL —
		// Windows uses the old British name; PostgreSQL 15 only knows Asia/Kolkata.
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(AiwebsitebuilderApplication.class, args);
	}

}
