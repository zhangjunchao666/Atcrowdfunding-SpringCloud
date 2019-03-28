package com.atguigu.atcrowdfunding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import feign.Retryer;


@ServletComponentScan
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class AtcrowdfundingBootPortalApplication {

	@Bean
	public Retryer feignRetryer() {
		return Retryer.NEVER_RETRY;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AtcrowdfundingBootPortalApplication.class, args);
	}
}
