package com.atguigu.atcrowdfunding.controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.MemberCert;
import com.atguigu.atcrowdfunding.bean.Ticket;
import com.atguigu.atcrowdfunding.service.ActivitiService;
import com.atguigu.atcrowdfunding.service.MemberService;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.util.Datas;

@Controller
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService ;
	
	@Autowired
	private ActivitiService activitiService ;
	
	
	@ResponseBody
	@RequestMapping("/member/completeApply")
	public Object completeApply( HttpSession session, String authcode ) {
		start();	 
		try {
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			
			Ticket ticket = memberService.queryTicketByMemberId(loginMember.getId());
			
			if(ticket.getAuthcode().equals(authcode)) {
				loginMember.setAuthstatus("1");
				int count = memberService.completeApply(loginMember);
				if(count==1) {
					success(true);
					message("验证码验证成功!");
				}else {
					success(false);
					message("业务操作失败!");
				}
			}else {
				success(false);
				message("验证码错误!");
			}
		} catch ( Exception e ) {
			success(false);
			message(e.getMessage());
			e.printStackTrace();
		}
		 
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/member/checkEmail")
	public Object checkEmail( HttpSession session, String email ) {
		start();	 
		try {
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			loginMember.setEmail(email);
			
			session.setAttribute("loginMember", loginMember); //Session共享,需要同步redis缓存数据.
			
			int count = memberService.updateEmail(loginMember);
			success(count == 1);
		} catch ( Exception e ) {
			success(false);
			message(e.getMessage());
			e.printStackTrace();
		}
		 
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/member/certUpload")
	public Object certUpload(HttpSession session,Datas ds) {
		start();	 
		try {
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);

			List<MemberCert> memberCertList = ds.getMemberCertList();
			for (MemberCert memberCert : memberCertList) {
				Integer certid = memberCert.getCertid();
				MultipartFile certFile = memberCert.getCertFile(); //将文件另存到静态资源服务路径下。
				
				String originalFilename = certFile.getOriginalFilename(); //原始文件名
				
				String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf(".")); 
			
				String filepath = "D:\\resources\\atcrowdfunding\\pics\\cert\\"+filename ;
				
				certFile.transferTo(new File(filepath)); //另存为
				
				memberCert.setIconpath(filename);
				memberCert.setMemberid(loginMember.getId());
				memberCert.setCertFile(null); //文件已经另存到静态资源服务器上了，不需要传递给远程服务。
			}
			memberService.saveMemberCertList(memberCertList);
			
			success(true);
		} catch ( Exception e ) {
			success(false);
			message(e.getMessage());
			e.printStackTrace();
		}
		 
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/member/uploadBasicinfo")
	public Object uploadBasicinfo( HttpSession session, Member member ) {
		start();	 
		try {
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			loginMember.setRealname(member.getRealname());
			loginMember.setCardnum(member.getCardnum());
			loginMember.setTel(member.getTel());
			
			session.setAttribute("loginMember", loginMember); //Session共享,需要同步redis缓存数据.
			
			int count = memberService.uploadBasicinfo(loginMember);
			success(count == 1);
		} catch ( Exception e ) {
			success(false);
			message(e.getMessage());
			e.printStackTrace();
		}
		 
		return end();
	}

	
	
	@ResponseBody
	@RequestMapping("/member/accttypeApply")
	public Object accttypeApply( HttpSession session, String accttype ) {
		start();	 
		try {
			Member loginMember = (Member)session.getAttribute(Const.LOGIN_MEMBER);
			loginMember.setAccttype(accttype);
			session.setAttribute("loginMember", loginMember); //Session共享,需要同步redis缓存数据.
			
			int count = memberService.updateAccttype(loginMember);
			success(count == 1);
		} catch ( Exception e ) {
			success(false);
			message(e.getMessage());
			e.printStackTrace();
		}
		 
		return end();
	}

	
	
	
	@RequestMapping("/member/apply")
	public String apply(HttpSession session,Map<String,Object> map) {
		// 1.点击【未实名认证】
		// 2.查询流程单
		Member member = (Member)session.getAttribute(Const.LOGIN_MEMBER);
		
		Ticket ticket = memberService.queryTicketByMemberId(member.getId());

		// ① 流程单null (刚开始申请)
		if(ticket == null) {
			// 启动流程实例（流程定义部署）
			String piid = activitiService.startProcessInstance(member.getLoginacct());
			// 保存流程单
			ticket = new Ticket();
			ticket.setMemberid(member.getId());
			ticket.setPiid(piid);
			ticket.setStatus("0");
			ticket.setPstep("accttype");
			
			memberService.saveTicket(ticket);
			
			// 跳转到账户类型选择页面（第一个页面）
			return "member/accttype";
		}else {
			// ② 流程单不为null (申请过，但没有申请完成，继续申请)
			// 跳转到继续申请的页面
			String pstep = ticket.getPstep();
			if ( "accttype".equals(pstep) ) {
				return "member/accttype";
			} else if ( "basicinfo".equals(pstep) ) {
				return "member/basicinfo";
			} else if ( "certupload".equals(pstep) ) {
				
				String accttype = member.getAccttype();
				List<Cert> certList =  memberService.queryCertByAccttype(accttype);
				map.put("certList", certList);
				
				return "member/certupload";
			} else if ( "checkemail".equals(pstep) ) {
				return "member/checkemail";
			} else if ( "checkauthcode".equals(pstep) ) {
				return "member/checkauthcode";
			}

		}
		return "member/accttype" ;
	}
	
}
