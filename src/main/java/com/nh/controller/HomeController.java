package com.nh.controller;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nh.service.BoardService;

@Controller
public class HomeController {
	@Autowired
	BoardService bSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/")
	public String home(Integer pageNum, HttpSession session, Model model) {
		// 임시 로그인 상태
		session.setAttribute("loginId", 1);
		
		int page = 1;
		
		// 페이지
		if(pageNum!=null) page = pageNum;
		
		// 최신 게시글 조회
		model.addAttribute("boards",bSvc.getBoardsLastestOrder((Integer)session.getAttribute("loginId"), page));
		
		return "MainPage";
	}
	
}
