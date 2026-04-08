package com.example.api_gateway.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;



public class RateLimitingFilterTokenBucketALGO implements GlobalFilter,Ordered {

	private static final Logger log = LoggerFactory.getLogger(RateLimitingFilterTokenBucketALGO.class);
	
	private static final int bucket_size = 5; //Size of bucket
	private static final int refill_rate = 2; //Refilling bucket in time
	
	
	private Map<String, Integer> tokens = new ConcurrentHashMap<>();
	private Map<String, Long> lastFills = new ConcurrentHashMap<>();
	
	
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		String clientIP = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
		String api = exchange.getRequest().getURI().getPath().split("/")[1];
		String key = clientIP+"::"+api;
		
		long current_time = System.currentTimeMillis();
		tokens.putIfAbsent(key, bucket_size);
		lastFills.putIfAbsent(key, current_time);
		
		long lastTime = lastFills.get(key);
		int currentToken = tokens.get(key);
		
		// calculate tokens to refill
        long timeElapsed = current_time - lastTime;
        int tokensToAdd = (int) (timeElapsed / 10000 * refill_rate);
        
		if(tokensToAdd > 0)
		{
			currentToken = Math.min(bucket_size,currentToken+tokensToAdd);
			tokens.put(key, currentToken);
			lastFills.put(key, current_time);
		}
		
		if(currentToken <= 0) {
			log.warn("Rate limit exceeded for key: {}", key);
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders()
                    .add("X-RateLimit-Retry-After", String.valueOf(10000 / 1000) + "s");
            return exchange.getResponse().setComplete();
		}
		tokens.put(key, currentToken-1);
		
		exchange.getResponse().getHeaders()
        	.add("X-RateLimit-Remaining", String.valueOf(currentToken - 1));
		exchange.getResponse().getHeaders()
        	.add("X-RateLimit-Limit", String.valueOf(bucket_size));

		log.info("key={} | tokens remaining={} | elapsed={}ms | added={}",key,currentToken - 1,timeElapsed,tokensToAdd);
		
		
//		log.info("Bucket Size :::--> "+bucket_size);
//		log.info("Refill Rate :::--> "+refill_rate);
//		log.info("Tokens ::--> "+tokens);
//		log.info("lastfills ::--> "+lastFills);
//		log.info("Client IP ::--> "+clientIP);
//		log.info("Api ::--> "+api);
//		log.info("key ::--> "+key);
//		log.info("Current Time ::--> "+current_time);
//		log.info("Time Elapsed ::--> "+lastTime);
//      log.info("Tokens To Add ::--> "+currentToken);
//		log.info("Time Elapsed ::--> "+timeElapsed);
//      log.info("Tokens To Add ::--> "+tokensToAdd);
		
		
		return chain.filter(exchange);
	}



	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}
