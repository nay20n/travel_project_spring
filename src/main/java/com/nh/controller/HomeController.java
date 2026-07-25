package com.nh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nh.service.BoardService;
import com.nh.service.MemberService;

@Controller
public class HomeController {
	@Autowired
	BoardService bSvc;
	@Autowired
	MemberService mSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/")
	public String home(Integer pageNum, HttpSession session, Model model) {
		session.setAttribute("loginId", 1);
		
		// 페이지
		int page = 1;
		if(pageNum!=null) page = pageNum;
		
		// 최신 게시글 조회
		model.addAttribute("boards",bSvc.getBoardsLastestOrder((Integer)session.getAttribute("loginId"), page));
		
		return "MainPage";
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(RedirectAttributes rttr, HttpSession session) {
		session.invalidate();
		rttr.addFlashAttribute("msg", "로그아웃 되었습니다.");
		return "redirect:/";
	}
	
	// 로그인
	@PostMapping("/login")
	public String login(@RequestParam String inputId, @RequestParam String inputPw, RedirectAttributes rttr, HttpSession session) {
		if(mSvc.canLogin(inputId, inputPw)==null) {
			rttr.addFlashAttribute("msg", "정보를 찾을 수 없습니다. 정확한 정보를 입력해주세요.");
			return "redirect:/";
		}
		session.setAttribute("loginId", mSvc.canLogin(inputId, inputPw));
		rttr.addFlashAttribute("msg", "로그인 되었습니다.");
		return "redirect:/";
	}
}
