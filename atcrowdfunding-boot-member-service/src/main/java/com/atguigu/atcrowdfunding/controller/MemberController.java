package com.atguigu.atcrowdfunding.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.MemberCert;
import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.service.MemberService;

@RestController
public class MemberController extends BaseController{

	@Autowired
	private MemberService memberService ;

	@RequestMapping("/member/updateMemberAuthstatus/{memberId}/{authstatus}")
	public void updateMemberAuthstatus(
				@PathVariable("memberId") Integer memberId,
				@PathVariable("authstatus") String authstatus) {
		memberService.updateMemberAuthstatus(memberId,authstatus);
	}
	
	@RequestMapping("/member/getMemberById/{memberId}")
	public Member getMemberById(@PathVariable("memberId") Integer memberId) {
		return memberService.getMemberById(memberId);
	}

	@RequestMapping("/member/queryCertInfoListByMemberId/{memberId}")
	public List<Map<String, Object>> queryCertInfoListByMemberId(@PathVariable("memberId") Integer memberId){
		return memberService.queryCertInfoListByMemberId(memberId);
	}
	
	
	
	
	
	
	@RequestMapping("/member/queryMemberByPiid/{piid}")
	public Member queryMemberByPiid(@PathVariable("piid") String piid) {
		return memberService.queryMemberByPiid(piid);
	}
	
	
	
	@RequestMapping("/member/completeApply")
	public int completeApply(@RequestBody Member loginMember) {
		return memberService.completeApply(loginMember);
	}
	
	@RequestMapping("/member/updateEmail")
	public int updateEmail(@RequestBody Member loginMember) {
		return memberService.updateEmail(loginMember);
	}
	
	@RequestMapping("/member/saveMemberCertList")
	public void saveMemberCertList(@RequestBody List<MemberCert> memberCertList) {
		memberService.saveMemberCertList(memberCertList);
	}
	
	@RequestMapping("/member/queryCertByAccttype/{accttype}")
	public List<Cert> queryCertByAccttype(@PathVariable("accttype") String accttype){
		return memberService.queryCertByAccttype(accttype);
	}
	
	
	@RequestMapping("/member/uploadBasicinfo")
	public int uploadBasicinfo(@RequestBody Member loginMember) {
		return memberService.uploadBasicinfo(loginMember);
	}
	
	@RequestMapping("/member/updateAccttype")
	public int updateAccttype(@RequestBody Member loginMember) {
		return memberService.updateAccttype(loginMember);
	}
	
	
	@RequestMapping("/member/queryMemberByLogin/{loginacct}")
	public Member queryMemberByLogin(@PathVariable("loginacct") String loginacct) {
		Member member = memberService.queryMemberByLogin(loginacct);
		return member ;
	}
	
	
	@RequestMapping("/member/queryTicketByMemberId/{memberId}")
	public Ticket queryTicketByMemberId(@PathVariable("memberId") Integer memberId) {
		return memberService.queryTicketByMemberId(memberId);
	}

	@RequestMapping("/member/saveTicket")
	public void saveTicket(@RequestBody Ticket ticket) {
		memberService.saveTicket(ticket);
	}
}
