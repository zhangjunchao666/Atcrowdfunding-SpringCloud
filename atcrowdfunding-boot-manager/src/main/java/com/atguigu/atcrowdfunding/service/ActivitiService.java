package com.atguigu.atcrowdfunding.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("atcrowdfunding-boot-activiti-service")
public interface ActivitiService {

	//1.简单参数，使用路径占位符，@PathViriable注解操作参数
	//2.复杂类型参数，使用@RequestBody注解，将提交参数数据以请求体的方式提交给远程服务。
	
	@RequestMapping("/activiti/queryProcessDefList")
	public List<Map<String, Object>> queryProcessDefList(@RequestBody Map<String, Object> paramMap);

	@RequestMapping("/activiti/queryProcesDefCount")
	public Integer queryProcesDefCount(@RequestBody Map<String, Object> paramMap);

	@RequestMapping("/activiti/deleteProcDef/{deployId}")
	public void deleteProcDef(@PathVariable("deployId") String deployId);

	
	@RequestMapping("/activiti/queryTaskList")
	public List<Map<String, Object>> queryTaskList(@RequestBody Map<String, Object> paramMap);

	@RequestMapping("/activiti/queryTaskCount")
	public Integer queryTaskCount(@RequestBody Map<String, Object> paramMap);

	@RequestMapping("/activiti/passApply")
	public void passApply(@RequestBody Map<String, Object> map);

}
