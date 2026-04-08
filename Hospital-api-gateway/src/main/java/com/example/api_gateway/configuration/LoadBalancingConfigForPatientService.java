package com.example.api_gateway.configuration;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@LoadBalancerClients({
    @LoadBalancerClient(name = "PATIENT-MICROSERVICES", configuration = LoadBalancingConfigForPatientService.class),
    @LoadBalancerClient(name = "DOCTOR-MICROSERVICES", configuration = LoadBalancingConfigForPatientService.class)
})
public class LoadBalancingConfigForPatientService {

	@Bean
	public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment env, LoadBalancerClientFactory factory)
	{
		
		String name = env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		
		return new RandomLoadBalancer(
	            factory.getLazyProvider(name, ServiceInstanceListSupplier.class),
	            name
	        );
	}
}
