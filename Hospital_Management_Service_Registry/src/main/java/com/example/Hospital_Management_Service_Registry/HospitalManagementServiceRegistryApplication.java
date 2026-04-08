package com.example.Hospital_Management_Service_Registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class HospitalManagementServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalManagementServiceRegistryApplication.class, args);
	}

}
