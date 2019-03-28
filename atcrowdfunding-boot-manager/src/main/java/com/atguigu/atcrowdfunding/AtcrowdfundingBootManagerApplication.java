package com.atguigu.atcrowdfunding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class AtcrowdfundingBootManagerApplication {

	//  <bean id="restTemplate" class="org.springframework.web.client.RestTemplate"></bean>
	@LoadBalanced //负载均衡
	@Bean
	public RestTemplate restTemplate() { //RestTemplate对用用于调用远程服务。也会涉及负载均衡问题。需要增加@LoadBalanced注解
		return new RestTemplate();
	}

	
	public static void main(String[] args) {
		SpringApplication.run(AtcrowdfundingBootManagerApplication.class, args);
	}
}
