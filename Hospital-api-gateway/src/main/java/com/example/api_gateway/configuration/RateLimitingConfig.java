package com.example.api_gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.api_gateway.filter.RateLimitingFilterFixedWindowALGO;
import com.example.api_gateway.filter.RateLimitingFilterTokenBucketALGO;

@Configuration	
public class RateLimitingConfig {

	
	@Bean
	public RateLimitingFilterTokenBucketALGO rateLimitTokenBucket() {
		return new RateLimitingFilterTokenBucketALGO();
	}
}
