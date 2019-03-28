package com.atguigu.atcrowdfunding.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.service.MemberService;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.util.MD5Util;

@Controller
public class DispatcherController extends BaseController {

	@Autowired
	private MemberService memberService ;
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/member")
	public String member() {
		return "member";
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		if(session!=null) {
			session.invalidate();
		}
		return "redirect:/login";
	}
	
	@ResponseBody
	@RequestMapping("/doLogin")
	public Object doLogin(Member member,HttpSession session) {
		start();
		try {
			Member loginMember = memberService.queryMemberByLogin(member.getLoginacct());
			
			if(loginMember == null) {
				message(Const.LOGIN_LOGINACCT_ERROR);
				success(false);
				return end();
			}
			
			if(!loginMember.getUserpswd().equals(MD5Util.digest(member.getUserpswd()))) {
				message(Const.LOGIN_USERPSWD_ERROR);
				success(false);
				return end();
			}
			
			session.setAttribute(Const.LOGIN_MEMBER, loginMember);
			success(true);
		} catch (Exception e) {
			success(false);
			message(e.getMessage());
			e.printStackTrace();
		}
		return end();
	}
	
	
	
}
