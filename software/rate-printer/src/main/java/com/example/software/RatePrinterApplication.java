package com.example.software;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ProviderProperties.class)
public class RatePrinterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatePrinterApplication.class, args);
	}
}
