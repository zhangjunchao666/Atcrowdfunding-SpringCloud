package com.atguigu.atcrowdfunding.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.atcrowdfunding.bean.Member;

@FeignClient("atcrowdfunding-boot-member-service")
public interface MemberService {

	@RequestMapping("/member/getMemberById/{memberId}")
	public Member getMemberById(@PathVariable("memberId") Integer memberId);

	@RequestMapping("/member/queryCertInfoListByMemberId/{memberId}")
	public List<Map<String, Object>> queryCertInfoListByMemberId(@PathVariable("memberId") Integer memberId);


}
