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
		return "Board";
	}
	
	@GetMapping("/plan/{bno}/week")
	public String planDetailWeek(@RequestParam(required = false) String key, @PathVariable int bno, RedirectAttributes rttr, HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		Map<String, Object> map1 = bSvc.getBoardInfo(loginId, bno);
		BigDecimal shareUserId = (BigDecimal)map1.get("shareUserId");
		// 공유 링크로 들어온 경우
		if(key!=null) {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			Timestamp date = (Timestamp)map1.get("exDate");
			if(date!=null&&map1.get("key")!=null
					&&shareUserId==null&&((BigDecimal)map1.get("writerId")).intValue()!=loginId
					&&now.before(date)
					&&key.equals(map1.get("key"))) {
				map1 = bSvc.addSharedMember(loginId, bno, key);
			}
			//System.out.println(date.getClass());
			//System.out.println(date);
		}
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
	public String planDetailMonth(@RequestParam(required = false) String key, @PathVariable int bno, RedirectAttributes rttr, HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		Map<String, Object> map1 = bSvc.getBoardInfo(loginId, bno);
		BigDecimal shareUserId = (BigDecimal)map1.get("shareUserId");
		// 공유 링크로 들어온 경우 
		if(key!=null) {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			Timestamp date = (Timestamp)map1.get("exDate");
			if(date!=null&&map1.get("key")!=null
					&&shareUserId==null&&((BigDecimal)map1.get("writerId")).intValue()!=loginId
					&&now.before(date)
					&&key.equals(map1.get("key"))) {
				map1 = bSvc.addSharedMember(loginId, bno, key);
			}
		}
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
