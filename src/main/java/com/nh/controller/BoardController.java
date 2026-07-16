package com.nh.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
	public String planDetailWeek(@PathVariable int bno, HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		model.addAllAttributes(bSvc.getBoardInfo(loginId, bno));
		
		return "MainWeek";
	}
	
	@GetMapping("/plan/{bno}/month")
	public String planDetailMonth(@PathVariable int bno, HttpSession session, Model model) {
		return "MainMonth";
	}
	
	@GetMapping("/plan/{bno}/day")
	public String planDetailDay(@PathVariable int bno, HttpSession session, Model model) {
		model.addAllAttributes(bSvc.getBoardInfo((int)session.getAttribute("loginId"), bno));
		return "MainDay";
	}
}
