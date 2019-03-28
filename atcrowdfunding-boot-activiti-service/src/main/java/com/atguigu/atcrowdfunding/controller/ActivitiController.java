package com.atguigu.atcrowdfunding.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.service.MemberService;

@RestController
public class ActivitiController extends BaseController {

	@Autowired
	private RepositoryService repositoryService; // IOC容器会自动创建好7个服务对象，直接进行依赖注入就可以了。

	@Autowired
	private RuntimeService runtimeService ;
	
	@Autowired
	private TaskService taskService ;
	
	@Autowired
	private MemberService memberService ;
	
	
	@RequestMapping("/activiti/passApply")
	public void passApply(@RequestBody Map<String, Object> map) {
		String taskId = (String)map.get("taskId");
		
		map.put("flag", true);
		
		taskService.complete(taskId, map);
	}
	
	@RequestMapping("/activiti/queryTaskList")
	public List<Map<String, Object>> queryTaskList(@RequestBody Map<String, Object> paramMap){
		Integer startIndex = (Integer)paramMap.get("startIndex");
		Integer pagesize = (Integer)paramMap.get("pagesize");
		
		List<Task> listPage = taskService.createTaskQuery()	
					.processDefinitionKey("workflow")
					.taskCandidateGroup("checkback")
					.listPage(startIndex, pagesize);
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		for (Task task : listPage) {
			//数据封装，列表上每一条数据封装map对象，数据来自三张表
			Map<String, Object> map = new HashMap<String,Object>();
			//1.封装流程定义数据
			String processDefinitionId = task.getProcessDefinitionId();
			ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
													.processDefinitionId(processDefinitionId)
													.singleResult();
			map.put("pdId", pd.getId());
			map.put("pdName", pd.getName());
			map.put("pdVersion", pd.getVersion());
			map.put("deployId", pd.getDeploymentId());
			
			//2.封装流程任务数据
			map.put("taskId", task.getId());
			map.put("taskName", task.getName());
			
			//3.封装会员信息数据
			String piid = task.getProcessInstanceId();
			Member member = memberService.queryMemberByPiid(piid);
			
			map.put("memberId", member.getId());
			map.put("username", member.getUsername());
			map.put("loginacct", member.getLoginacct());
			
			list.add(map);
		}
		return list ;
	}

	@RequestMapping("/activiti/queryTaskCount")
	public Integer queryTaskCount(@RequestBody Map<String, Object> paramMap) {
		Long count = taskService.createTaskQuery()	
								.processDefinitionKey("workflow")
								.taskCandidateGroup("checkback")
								.count();
		return count.intValue();
	}
	
	
	
	@RequestMapping("/activiti/nextStep")
	public void nextStep(@RequestBody Map<String, Object> paramMap) {
		
		String piid = (String)paramMap.get("piid");
		String loginacct = (String)paramMap.get("loginacct");
		
		
		Task task = taskService.createTaskQuery()
			.processInstanceId(piid)
			.taskAssignee(loginacct)
			.singleResult();
		
		taskService.complete(task.getId(), paramMap);
	}
	
	
	@RequestMapping("/activiti/startProcessInstance/{loginacct}")
	public String startProcessInstance(@PathVariable("loginacct") String loginacct) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionKey("workflow")
			.latestVersion()
			.singleResult();
		
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("loginacct", loginacct);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
		return processInstance.getId();
	}
	
	
	
	//================================================================
	
	
	@RequestMapping("/activiti/deleteProcDef/{deployId}")
	public void deleteProcDef(@PathVariable("deployId") String deployId) {
		repositoryService.deleteDeployment(deployId, true);//true 表示级联删除  act_re_deploment act_ge_bytearray act_re_procdef
	}
	
	
	@RequestMapping("/activiti/loadImgByPdId/{pdId}")
	public byte[] loadImgByPdId(@PathVariable("pdId") String pdId) {
		// 部署ID ==> 流程定义ID
		// 从数据库中读取流程定义的图片
		// 根据流程部署id和部署资源名称获取部署图片的输入流。
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd = query.processDefinitionId(pdId).singleResult();
		
		InputStream in = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());

		ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); //内存流
		byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
		int rc = 0;
		try {
			while ((rc = in.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] in_b = swapStream.toByteArray(); // in_b为转换之后的结果

		return in_b;

	}

	/*
	 * @RequestMapping("/activiti/uploadProcDefFile") public String
	 * uploadProcDefFile(HttpServletRequest request) {
	 * 
	 * MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
	 * MultipartFile file = mreq.getFile("pdfile"); }
	 */

	@RequestMapping("/activiti/uploadProcDefFile")
	public String uploadProcDefFile(@RequestParam("pdfile") MultipartFile pdfile) {
		// Deployment deploy =
		// repositoryService.createDeployment().addClasspathResource("").deploy();
		try {
			Deployment deploy = repositoryService.createDeployment()
					.addInputStream(pdfile.getOriginalFilename(), pdfile.getInputStream()).deploy();
			System.out.println(deploy);
			return "部署成功!";
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "部署失败!";
	}

	// Could not write JSON: Direct self-reference leading to cycle;
	// nested exception is com.fasterxml.jackson.databind.JsonMappingException:
	// Direct self-reference leading to cycle

	// @RequestMapping("/activiti/queryProcessDefList/{startIndex}/{pagesize}")
	@RequestMapping("/activiti/queryProcessDefList")
	public List<Map<String, Object>> queryProcessDefList(@RequestBody Map<String, Object> paramMap) {
		// public List<ProcessDefinition>
		// queryProcessDefList(@PathVariable("startIndex") Integer startIndex
		// ,@PathVariable("pagesize") Integer pagesize){
		// public List<Map<String,Object>>
		// queryProcessDefList(@PathVariable("startIndex") Integer startIndex
		// ,@PathVariable("pagesize") Integer pagesize){

		Integer startIndex = (Integer) paramMap.get("startIndex");
		Integer pagesize = (Integer) paramMap.get("pagesize");

		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		List<ProcessDefinition> listPage = processDefinitionQuery.listPage(startIndex, pagesize);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (ProcessDefinition pd : listPage) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pdName", pd.getName());
			map.put("pdVersion", pd.getVersion());
			map.put("pdKey", pd.getKey());
			map.put("pdId", pd.getId());

			map.put("deployId", pd.getDeploymentId());
			
			list.add(map);
		}

		return list;
	}

	@RequestMapping("/activiti/queryProcesDefCount")
	public Integer queryProcesDefCount(@RequestBody Map<String, Object> paramMap) {

		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		Long count = processDefinitionQuery.count();

		return count.intValue();
	}

}
