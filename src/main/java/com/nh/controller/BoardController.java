package com.nh.controller;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
		int loginId = (int)session.getAttribute("loginId");
		model.addAllAttributes(bSvc.getBoardInfo(loginId, bno));
		//System.out.println(bSvc.getBoardInfo(loginId, bno));
		return "Board";
	}
	
	@GetMapping("/share")
	public String planDetailWeekShare(int bno, String key, HttpSession session, RedirectAttributes rttr) {
		Integer loginId = (Integer)session.getAttribute("loginId");
		if(loginId==null) {
			rttr.addFlashAttribute("msg", "로그인 필요한 서비스입니다. 로그인 후 다시 진행해주세요.");
			return "redirect:/";
		}
		
		bSvc.addSharedMember(loginId, bno, key);
		
		rttr.addFlashAttribute("msg", "공동작업이 추가되었습니다. 마이페이지를 확인해주세요.");
		return "redirect:/";
	}
	
	@GetMapping("/plan/{bno}/week")
	public String planDetailWeek(@PathVariable int bno, RedirectAttributes rttr, HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		Map<String, Object> map1 = bSvc.getBoardInfo(loginId, bno);
		BigDecimal shareUserId = (BigDecimal)map1.get("shareUserId");
		if(shareUserId==null) {
			if(((BigDecimal)map1.get("writerId")).intValue()==loginId) {
				model.addAllAttributes(map1);
				return "MainWeek";
			}
		} else {
			if(shareUserId.intValue()==loginId || ((BigDecimal)map1.get("writerId")).intValue()==loginId) {
				model.addAllAttributes(map1);
				return "MainWeek";
			}
		}
		rttr.addFlashAttribute("msg", "잘못된 접근입니다.");
		return "redirect:/";
	}
	
	@GetMapping("/plan/{bno}/month")
	public String planDetailMonth(@PathVariable int bno, RedirectAttributes rttr, HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		Map<String, Object> map1 = bSvc.getBoardInfo(loginId, bno);
		BigDecimal shareUserId = (BigDecimal)map1.get("shareUserId");
		if(shareUserId==null) {
			if(((BigDecimal)map1.get("writerId")).intValue()==loginId) {
				model.addAllAttributes(map1);
				return "MainMonth";
			}
		} else {
			if(shareUserId.intValue()==loginId || ((BigDecimal)map1.get("writerId")).intValue()==loginId) {
				model.addAllAttributes(map1);
				return "MainMonth";
			}
		}
		rttr.addFlashAttribute("msg", "잘못된 접근입니다.");
		return "redirect:/";
	}
}
