package com.nh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@RequestMapping("/mypage")
	public String mypage() {
		return "MyPage";
	}
	
	@RequestMapping("/mypage/edit")
	public String edit() {
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
