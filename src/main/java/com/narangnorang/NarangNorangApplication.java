package com.narangnorang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:datasource.properties")
@PropertySource(value = "classpath:mail.properties")
public class NarangNorangApplication {

	public static void main(String[] args) {
		SpringApplication.run(NarangNorangApplication.class, args);
	}

}
