package com.example.Patient_Microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PatientMicroservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientMicroservicesApplication.class, args);
	}

}
