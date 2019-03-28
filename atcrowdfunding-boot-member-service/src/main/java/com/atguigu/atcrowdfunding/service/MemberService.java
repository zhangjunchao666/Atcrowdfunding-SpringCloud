package com.atguigu.atcrowdfunding.service;

import java.util.List;
import java.util.Map;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.MemberCert;
import com.atguigu.atcrowdfunding.bean.Ticket;

public interface MemberService {

	Member queryMemberByLogin(String loginacct);

	Ticket queryTicketByMemberId(Integer memberId);

	void saveTicket(Ticket ticket);

	int updateAccttype(Member loginMember);

	int uploadBasicinfo(Member loginMember);

	List<Cert> queryCertByAccttype(String accttype);

	void saveMemberCertList(List<MemberCert> memberCertList);

	int updateEmail(Member loginMember);

	int completeApply(Member loginMember);

	Member queryMemberByPiid(String piid);

	Member getMemberById(Integer memberId);

	List<Map<String, Object>> queryCertInfoListByMemberId(Integer memberId);

	void updateMemberAuthstatus(Integer memberId, String authstatus);


}
