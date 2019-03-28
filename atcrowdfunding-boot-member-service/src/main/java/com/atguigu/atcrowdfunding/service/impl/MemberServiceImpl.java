package com.atguigu.atcrowdfunding.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.MemberCert;
import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.dao.MemberDao;
import com.atguigu.atcrowdfunding.service.ActivitiService;
import com.atguigu.atcrowdfunding.service.MemberService;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private ActivitiService activitiService ;
	
	@Autowired
	private MemberDao memberDao ;

	@Override
	public Member queryMemberByLogin(String loginacct) {
		return memberDao.queryMemberByLogin(loginacct);
	}

	@Override
	public Ticket queryTicketByMemberId(Integer memberId) {
		return memberDao.queryTicketByMemberId(memberId);
	}

	@Override
	public void saveTicket(Ticket ticket) {
		memberDao.saveTicket(ticket);
	}

	@Override
	public int updateAccttype(Member loginMember) {
		// 1.先选择图片（选择账户类型）
		// 2.点击【认证申请】
		// ①如果没有选择账户类型，给予友好提示
		// ②如果选择账户类型，将账户类型值提交给后台
		
		// 更新t_member accttype='2'
		int i = memberDao.updateAccttype(loginMember);
		
		// 更新t_ticket pstep='basicinfo'		
		Ticket ticket = memberDao.getTicketByMemberId(loginMember.getId());
		ticket.setPstep("basicinfo");
		
		memberDao.updateTicket(ticket);
		
		// 流程步骤往下执行一步（传两个参数，piid,loginacct）
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("piid", ticket.getPiid());
		paramMap.put("loginacct", loginMember.getLoginacct());
		activitiService.nextStep(paramMap);

		return i;
	}

	@Override
	public int uploadBasicinfo(Member loginMember) {
		//将realname   cardnum   tel  更新 t_member 表中
		int i = memberDao.uploadBasicinfo(loginMember);
		
		// 更新t_ticket  pstep='certupload'
		Ticket ticket = memberDao.getTicketByMemberId(loginMember.getId());
		ticket.setPstep("certupload");
		
		memberDao.updateTicket(ticket);
		
		// 流程步骤往下执行一步（传两个参数，piid,loginacct,flag）
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("piid", ticket.getPiid());
		paramMap.put("loginacct", loginMember.getLoginacct());
		paramMap.put("flag", true);
		activitiService.nextStep(paramMap);
		return i ;
	}

	@Override
	public List<Cert> queryCertByAccttype(String accttype) {
		return memberDao.queryCertByAccttype(accttype);
	}

	@Override
	public void saveMemberCertList(List<MemberCert> memberCertList) {
		MemberCert memberCert = memberCertList.get(0);
		Integer memberid = memberCert.getMemberid();
		
		//保存用户和 资质关系数据
		memberDao.saveMemberCertList(memberCertList);
		
		// 更新t_ticket  pstep='checkemail'
		Ticket ticket = memberDao.getTicketByMemberId(memberid);
		ticket.setPstep("checkemail");
		
		memberDao.updateTicket(ticket);
		
		// 流程步骤往下执行一步（传两个参数，piid,loginacct,flag）
		Member member = memberDao.getMemberById(memberid);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("piid", ticket.getPiid());
		paramMap.put("loginacct", member.getLoginacct());
		paramMap.put("flag", true);
		activitiService.nextStep(paramMap);
	}

	@Override
	public int updateEmail(Member loginMember) {
		
		System.out.println("*************updateEmail****************");
		
		//1.更新邮箱（邮箱变了需要更新，否则不需要更新）
		int count = memberDao.updateEmail(loginMember);
		
		//2.更新流程单步骤，验证码
		Ticket ticket = memberDao.getTicketByMemberId(loginMember.getId());
		ticket.setPstep("checkauthcode");
		
		StringBuilder str = new StringBuilder();
		for(int i=1; i<=4; i++) {
			str.append(new Random().nextInt(10));
		}
		String authcode = str.toString();
		
		ticket.setAuthcode(authcode);
		memberDao.updateTicketPstepAndAuthcode(ticket);
		
		//3.流程继续往下走一步
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("piid", ticket.getPiid());
		paramMap.put("loginacct", loginMember.getLoginacct());
		paramMap.put("flag", true);
		
		paramMap.put("email", loginMember.getEmail());
		paramMap.put("authcode", authcode);
		
		
		activitiService.nextStep(paramMap);
		
		System.out.println("*************updateEmail2****************");
		return count;
	}

	@Override
	public int completeApply(Member loginMember) {
		
		//1.修改会员authstatus="1"
		int count = memberDao.updateAuthstatus(loginMember);
		
		//2.修改流程单status="1" 
		Ticket ticket = memberDao.getTicketByMemberId(loginMember.getId());
		ticket.setStatus("1");		
		memberDao.updateTicketStatus(ticket);
		
		//3.流程继续往下走一步
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("piid", ticket.getPiid());
		paramMap.put("loginacct", loginMember.getLoginacct());

		activitiService.nextStep(paramMap);
		
		return count;
	}

	@Override
	public Member queryMemberByPiid(String piid) {
		return memberDao.queryMemberByPiid(piid);
	}

	@Override
	public Member getMemberById(Integer memberId) {
		return memberDao.getMemberById(memberId);
	}

	@Override
	public List<Map<String, Object>> queryCertInfoListByMemberId(Integer memberId) {
		return memberDao.queryCertInfoListByMemberId(memberId);
	}

	@Override
	public void updateMemberAuthstatus(Integer memberId, String authstatus) {
		memberDao.updateMemberAuthstatus(memberId,authstatus);
	}


}
