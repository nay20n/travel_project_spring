package com.nh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nh.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	MemberService mSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@RequestMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		session.setAttribute("loginId", loginId);
		
		Map<String,Object> getMyPage = mSvc.getMyPage(loginId);
		model.addAttribute("getMyPage", getMyPage);
		
		return "MyPage";
	}
	
	@RequestMapping("/mypage/edit")
	public String edit(HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		session.setAttribute("loginId", loginId);
		
		Map<String, String> editPage = mSvc.getEditPage(loginId);
		model.addAttribute("editPage", editPage);
		
		return "EditInfo";
	}
	
	@RequestMapping("/forget")
	public String foget(HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		
		String email = mSvc.getEmail(loginId);
		String nickName = mSvc.getNickName(loginId);
		
		model.addAttribute("nickName", nickName);
		model.addAttribute("email", email);
		
		return "ResetPw";
	}
	
	@RequestMapping("/setpw")
	public String setpw(HttpSession session, Model model, RedirectAttributes rttr ,@RequestParam("key") String key) {
		int loginId = (int)session.getAttribute("loginId");
        
		// 지금 현재 세션에 저장된 key여야지만 접근 가능
        String keySession = (String)session.getAttribute("key");
		if(!keySession.equals(key)) {
			rttr.addFlashAttribute("rttrMsg", "해당 사이트의 접근권한이 없습니다.");
			return "redirect:/mypage/edit";
		}
        
		// 키가 존재하는지 + 만료되지 않았는지 서비스에서 확인
        boolean isValid = mSvc.isValidCode(key);
        if (!isValid) {
        	rttr.addFlashAttribute("rttrMsg", "키가 만료되었습니다.");
        	return "redirect:/mypage/edit";
        }
        
        model.addAttribute("loginId", loginId);
        model.addAttribute("key", key);
        return "SetPw"; 
	}
	
}
