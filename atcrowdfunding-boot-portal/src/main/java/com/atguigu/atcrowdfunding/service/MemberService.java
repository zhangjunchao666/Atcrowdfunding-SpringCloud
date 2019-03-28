package com.atguigu.atcrowdfunding.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.MemberCert;
import com.atguigu.atcrowdfunding.bean.Ticket;

@FeignClient("atcrowdfunding-boot-member-service")
public interface MemberService {
	@RequestMapping("/member/queryMemberByLogin/{loginacct}")
	public Member queryMemberByLogin(@PathVariable("loginacct") String loginacct);

	@RequestMapping("/member/queryTicketByMemberId/{id}")
	public Ticket queryTicketByMemberId(@PathVariable("id") Integer id);

	@RequestMapping("/member/saveTicket")
	public void saveTicket(@RequestBody Ticket ticket);

	@RequestMapping("/member/updateAccttype")
	public int updateAccttype(@RequestBody Member loginMember);

	@RequestMapping("/member/uploadBasicinfo")
	public int uploadBasicinfo(@RequestBody Member loginMember);

	@RequestMapping("/member/queryCertByAccttype/{accttype}")
	public List<Cert> queryCertByAccttype(@PathVariable("accttype") String accttype);

	@RequestMapping("/member/saveMemberCertList")
	public void saveMemberCertList(@RequestBody List<MemberCert> memberCertList);

	@RequestMapping("/member/updateEmail")
	public int updateEmail(@RequestBody Member loginMember);

	@RequestMapping("/member/completeApply")
	public int completeApply(@RequestBody Member loginMember);
}
