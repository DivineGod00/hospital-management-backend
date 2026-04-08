package com.example.api_gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.api_gateway.filter.RateLimitingFilterFixedWindowALGO;
import com.example.api_gateway.filter.RateLimitingFilterTokenBucketALGO;

@Configuration
public class GatewayConfig {
//	
//	@Autowired
//	private RateLimitingFilterFixedWindowALGO rateLimitingFilter;
	
	@Autowired
	private RateLimitingFilterTokenBucketALGO rateLimitTokenALGO;
	
	@Bean
	protected RouteLocator customeRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				
				.route("PATIENT-MICROSERVICES",r -> r.path("/patient/**")
//						.filters(f-> f.filter(rateLimitTokenALGO))
						.uri("lb://PATIENT-MICROSERVICES"))	
				.route("DOCTOR-MICROSERVICES",r->r.path("/doctor/**")
//						.filters(f -> f.filter(rateLimitTokenALGO))
						.uri("lb://DOCTOR-MICROSERVICES"))
				.build();
	}
}