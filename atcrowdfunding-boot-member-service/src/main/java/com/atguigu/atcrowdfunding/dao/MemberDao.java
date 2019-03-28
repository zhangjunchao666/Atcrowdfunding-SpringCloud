package com.atguigu.atcrowdfunding.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.MemberCert;
import com.atguigu.atcrowdfunding.bean.Ticket;

public interface MemberDao {

	@Select("select * from t_member where loginacct=#{loginacct}")
	Member queryMemberByLogin(String loginacct);

	@Select("select * from t_ticket where memberid=#{memberId} and status='0'")
	Ticket queryTicketByMemberId(Integer memberId);

	@Insert("insert into t_ticket(memberid,piid,status,pstep) values(#{memberid},#{piid},#{status},#{pstep})")
	void saveTicket(Ticket ticket);

	@Update("update t_member set accttype=#{accttype} where id=#{id}")
	int updateAccttype(Member loginMember);

	@Select("select * from t_ticket where memberid=#{memberid} and status='0'")
	Ticket getTicketByMemberId(Integer memberid);

	@Update("update t_ticket set pstep=#{pstep} where id=#{id}")
	void updateTicket(Ticket ticket);

	@Update("update t_member set realname=#{realname},cardnum=#{cardnum},tel=#{tel} where id=#{id}")
	int uploadBasicinfo(Member loginMember);

	@Select("SELECT t_cert.* FROM t_account_type_cert , t_cert WHERE t_account_type_cert.certid=t_cert.id AND t_account_type_cert.accttype=#{accttype}")
	List<Cert> queryCertByAccttype(String accttype);

	@Select("select * from t_member where id=#{memberid}")
	Member getMemberById(Integer memberid);

	void saveMemberCertList(@Param("memberCertList") List<MemberCert> memberCertList);

	@Update("update t_member set email=#{email} where id=#{id}")
	int updateEmail(Member loginMember);

	@Update("update t_ticket set pstep=#{pstep},authcode=#{authcode} where id=#{id}")
	void updateTicketPstepAndAuthcode(Ticket ticket);

	@Update("update t_member set authstatus=#{authstatus} where id=#{id}")
	int updateAuthstatus(Member loginMember);

	@Update("update t_ticket set status=#{status} where id=#{id}")
	void updateTicketStatus(Ticket ticket);

	@Select("select t_member.* from t_ticket,t_member where t_member.id=t_ticket.memberid and piid=#{piid}")
	Member queryMemberByPiid(String piid);

	List<Map<String, Object>> queryCertInfoListByMemberId(Integer memberId);

	@Update("update t_member set authstatus=#{authstatus} where id=#{memberId}")
	void updateMemberAuthstatus(@Param("memberId") Integer memberId,@Param("authstatus") String authstatus);

}
