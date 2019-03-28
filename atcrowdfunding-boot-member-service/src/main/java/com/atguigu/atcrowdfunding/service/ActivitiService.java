package com.atguigu.atcrowdfunding.service;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("atcrowdfunding-boot-activiti-service")
public interface ActivitiService {

	@RequestMapping("/activiti/nextStep")
	public void nextStep(@RequestBody Map<String, Object> paramMap);

}
