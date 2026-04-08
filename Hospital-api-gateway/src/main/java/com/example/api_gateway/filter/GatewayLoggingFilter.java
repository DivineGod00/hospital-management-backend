package com.example.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GatewayLoggingFilter implements GlobalFilter, Ordered{

	private static final Logger log = LoggerFactory.getLogger(GatewayLoggingFilter.class);

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("Gateway Logger");
		String path = exchange.getRequest().getURI().getPath();
		String method = exchange.getRequest().getMethod().name();
		String clientIP = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
		
		log.info("API HIT -> Path: {} | Method: {} | ClientIP: {}",path,method,clientIP);
		return chain.filter(exchange)
				.then(Mono.fromRunnable(()-> {
					int status = exchange.getResponse().getStatusCode().value();
				log.info("API HIT -> Path: {} | Status: {}",path,status);	
				}));
	}

	
}
