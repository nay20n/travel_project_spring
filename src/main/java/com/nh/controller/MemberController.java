package com.nh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nh.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	MemberService mSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@RequestMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
		//임시 로그인
		int loginId = (int)session.getAttribute("loginId");
		session.setAttribute("loginId", loginId);
		
		Map<String,Object> getMyPage = mSvc.getMyPage(loginId);
		
		// 마이페이지 처음에 한번에 호출
		model.addAttribute("getMyPage", getMyPage);
		
		return "MyPage";
	}
	
	@RequestMapping("/mypage/edit")
	public String edit(HttpSession session, Model model) {
		//임시 로그인
		int loginId = (int)session.getAttribute("loginId");
		session.setAttribute("loginId", loginId);
		
		String nickName = mSvc.getNickName(loginId);
		String email = mSvc.getEmail(loginId);
		
		model.addAttribute("nickName",nickName);
		model.addAttribute("email",email);
		
		return "EditInfo";
	}
	
	@RequestMapping("/forget")
	public String foget() {
		return "ResetPw";
	}
	
	@RequestMapping("/setpw")
	public String setpw() {
		return "SetPw";
	}
	
}
