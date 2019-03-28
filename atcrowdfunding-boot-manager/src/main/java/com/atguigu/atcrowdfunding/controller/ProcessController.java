package com.atguigu.atcrowdfunding.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.atguigu.atcrowdfunding.service.ActivitiService;
import com.atguigu.atcrowdfunding.util.Page;

@Controller
public class ProcessController extends BaseController{

	@Autowired
	private ActivitiService activitiService ; //面向接口编程。推荐。
	
	@Autowired
	private RestTemplate restTemplate ; //面向工具类编程
	
	@RequestMapping("/process/index")
	public String index() {
		return "process/index";
	}
	
	@RequestMapping("/process/view")
	public String view() {
		return "process/view";
	}
	
	
	
	@ResponseBody
	@RequestMapping("/process/doDelete")
	public Object doDelete(String deployId) {
		start();
		try {
			
			activitiService.deleteProcDef(deployId);

			success(true);
		} catch (Exception e) {
			success(false);
			e.printStackTrace();
		}
		
		return end();
	}
	
	
	@RequestMapping("/process/loadImg/{pdId}")
	public void loadImg(@PathVariable("pdId") String pdId,HttpServletResponse resp) throws IOException {

		// 通过响应对象返回图形信息
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		 
		String url = "http://atcrowdfunding-boot-activiti-service/activiti/loadImgByPdId/"+pdId;
		ResponseEntity<byte[]> response = restTemplate.exchange( url, HttpMethod.POST,  new HttpEntity<byte[]>(headers), byte[].class); 
		byte[] result = response.getBody(); //图片以二进制方式返回
		 
		InputStream in = new ByteArrayInputStream(result); //内存流
		OutputStream out = resp.getOutputStream(); //将图片以流的形式写给浏览器。
		 
		int i = -1;
		while ( (i = in.read()) != -1 ) {
			out.write(i);
		} 

	}
	
	
	@ResponseBody
	@RequestMapping("/process/uploadProcDefFile")
	public Object uploadProcDefFile(HttpServletRequest request) {
		start();
		try {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;		 
			MultipartFile file = mreq.getFile("procDefFile");

			// 获取的是表单中文件域的name属性值
			System.out.println(file.getName());
			// 获取的是传递的文件名称
			System.out.println(file.getOriginalFilename());
			 
			String uuid = UUID.randomUUID().toString();
			String fileName = file.getOriginalFilename();
			final File tempFile = File.createTempFile(uuid, fileName.substring(fileName.lastIndexOf(".")));
			 
			file.transferTo(tempFile);  //将上传文件，另存到临时文件中。

			
			FileSystemResource resource = new FileSystemResource(tempFile);  
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();  
			param.add("pdfile", resource);
			
			//通过RestTemplate调用远程服务，对远程服务调用进行封装。
			//1.第一个参数表示远程服务的映射（服务名称+映射路径）。
			//2.表示提交的请求数据
			//3.表示调用远程服务映射方法的返回结果类型
			String s =	restTemplate.postForObject("http://atcrowdfunding-boot-activiti-service/activiti/uploadProcDefFile",param,String.class);

			System.out.println("s="+s);
			
			tempFile.delete();
			
			success(true);
		} catch (Exception e) {
			success(false);
			e.printStackTrace();
		}
		
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/process/queryPage")
	public Object queryPage(Integer pageno,Integer pagesize) {
		start();
		try {
			
			Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageno,pagesize);
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);
			paramMap.put("startIndex", page.getStartindex());
			
			List<Map<String,Object>> list =  activitiService.queryProcessDefList(paramMap);
			
			Integer totalsize = activitiService.queryProcesDefCount(paramMap);
			
			page.setDatas(list);
			page.setTotalsize(totalsize);
			
			data(page);
			
			success(true);
		} catch (Exception e) {
			success(false);
			e.printStackTrace();
		}
		
		return end();
	}
	
	
}
