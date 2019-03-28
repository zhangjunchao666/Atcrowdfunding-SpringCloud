package com.atguigu.atcrowdfunding.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("atcrowdfunding-boot-activiti-service")
public interface ActivitiService {

	@RequestMapping("/activiti/startProcessInstance/{loginacct}")
	public String startProcessInstance(@PathVariable("loginacct") String loginacct);

}
