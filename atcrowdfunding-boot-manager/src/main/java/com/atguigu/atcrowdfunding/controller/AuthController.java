package com.atguigu.atcrowdfunding.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.service.ActivitiService;
import com.atguigu.atcrowdfunding.service.MemberService;
import com.atguigu.atcrowdfunding.util.Page;

@Controller
public class AuthController extends BaseController {

	@Autowired
	private ActivitiService activitiService ;
	
	@Autowired
	private MemberService memberService ;
	
	
	@ResponseBody
	@RequestMapping("/auth/pass")
	public Object pass(String taskId,Integer memberId) {
		start();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("taskId", taskId);
			map.put("memberId", memberId);
			
			activitiService.passApply(map);
			
			success(true);
		} catch (Exception e) {
			success(false);
			e.printStackTrace();
		}
		
		return end();
	}
	
	
	@RequestMapping("/auth/view")
	public String view(Integer memberId,Map<String,Object> map) {
		//1.查询 会员基本信息数据
		Member member = memberService.getMemberById(memberId);
		
		//2.查询 会员提交资质图片信息
		List<Map<String,Object>> memberCertList =  memberService.queryCertInfoListByMemberId(memberId);
		
		map.put("member", member);
		map.put("memberCertList", memberCertList);
		
		return "auth/view";
	}
	
	
	
	@RequestMapping("/auth/index")
	public String index() {
		return "auth/index";
	}
	
	
	
	@ResponseBody
	@RequestMapping("/auth/queryPage")
	public Object queryPage(Integer pageno,Integer pagesize) {
		start();
		try {
			
			Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageno,pagesize);
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);
			paramMap.put("startIndex", page.getStartindex());
			
			List<Map<String,Object>> list =  activitiService.queryTaskList(paramMap);
			
			Integer totalsize = activitiService.queryTaskCount(paramMap);
			
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
