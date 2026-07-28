package com.nh.controller;


import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nh.service.BoardService;
import com.nh.service.CommentService;

@Controller
public class BoardController {
	@Autowired
	BoardService bSvc;
	@Autowired
	CommentService cSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@GetMapping("/plan/{bno}")
	public String planDetail(@PathVariable int bno, HttpSession session, Model model) {
		model.addAllAttributes(bSvc.getBoardInfo((int)session.getAttribute("loginId"), bno));
		return "Board";
	}
	
	@GetMapping("/plan/{bno}/week")
	public String planDetailWeek(@PathVariable int bno, RedirectAttributes rttr, HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		Map<String, Object> map1 = bSvc.getBoardInfo(loginId, bno);
		if(((BigDecimal)map1.get("writerId")).intValue()==loginId) {
			model.addAllAttributes(bSvc.getBoardInfo(loginId, bno));
			return "MainWeek";
		}
		rttr.addFlashAttribute("msg", "잘못된 접근입니다.");
		return "redirect:/";
	}
	
	@GetMapping("/plan/{bno}/month")
	public String planDetailMonth(@PathVariable int bno, RedirectAttributes rttr, HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		Map<String, Object> map1 = bSvc.getBoardInfo(loginId, bno);
		if(((BigDecimal)map1.get("writerId")).intValue()==loginId) {
			model.addAllAttributes(bSvc.getBoardInfo(loginId, bno));
			return "MainMonth";
		}
		rttr.addFlashAttribute("msg", "잘못된 접근입니다.");
		return "redirect:/";
	}
}
