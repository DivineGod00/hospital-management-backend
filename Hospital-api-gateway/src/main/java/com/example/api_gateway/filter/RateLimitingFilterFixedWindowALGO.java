package com.example.api_gateway.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import com.netflix.eureka.RateLimitingFilter;

import reactor.core.publisher.Mono;

//Fixed Window
public class RateLimitingFilterFixedWindowALGO implements GatewayFilter{

	private static final Logger log = LoggerFactory.getLogger(RateLimitingFilterFixedWindowALGO.class);

	private static final int Max_Request = 2;
	private static final long TIME_OUT=60000;
	
	private Map<String, Long> requestTime = new ConcurrentHashMap<>();
	private Map<String, Integer> requestCount = new ConcurrentHashMap<>();	
	
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		log.info("Request Time : "+requestTime);
		
		
		String client_IP = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
		log.info("Client IP: {}",client_IP);
		
		String api = exchange.getRequest().getURI().getPath().split("/")[1];
		log.info("Api: {}",api);
		
		String key = client_IP + ":" + api;
		log.info("key: {}",key);
		
		long current_Time = System.currentTimeMillis();
		log.info("Time in Millis: {}",current_Time);
		
		for(Map.Entry<String, Integer> entry : requestCount.entrySet()){
        	log.info("before putting request count {}");
        	log.info("key :{} | value :{}",entry.getKey(),entry.getValue());
        }
		for(Map.Entry<String, Long> entry : requestTime.entrySet()){
        	log.info("before putting request time {}");
        	log.info("key :{} | value :{}",entry.getKey(),entry.getValue());
        }
		
		requestTime.putIfAbsent(key, current_Time);
        requestCount.putIfAbsent(key, 0);
        
        for(Map.Entry<String, Integer> entry : requestCount.entrySet()){
        	log.info("request count {}");
        	log.info("key :{} | value :{}",entry.getKey(),entry.getValue());
        }
        for(Map.Entry<String, Long> entry : requestTime.entrySet()){
        	log.info("request time {}");
        	log.info("key :{} | value :{}",entry.getKey(),entry.getValue());
        }
        
        
        if (current_Time - requestTime.get(key) > TIME_OUT) {
            requestTime.put(key, current_Time);
            requestCount.put(key, 0);
        }

        int count = requestCount.get(key);
        log.info("count "+count);
        if (count >= Max_Request) {

            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        requestCount.put(key, count + 1);
		
		return chain.filter(exchange);
	}

}
