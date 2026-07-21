package com.nh.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nh.service.BoardService;
import com.nh.service.PlaceService;

@Controller
public class NewPlanController {
	@Autowired
	PlaceService pSvc;
	@Autowired
	BoardService bSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(NewPlanController.class);
	
	@RequestMapping("/newplan/arr")
	public String arr(Model model, HttpSession session) {
		
		//임시 로그인
		session.setAttribute("loginId", 1);
		
		List<Map<String, Object>> recommendedPlace = bSvc.viewRecommendedPlace();
		model.addAttribute("recommendedPlace",recommendedPlace);
		return "ArrPlace";
	}
	
	@RequestMapping("/newplan/start")
	public String start() {
		return "StartPlace";
	}
	
	@RequestMapping("/newplan/date")
	public String date(@RequestParam String arrId, Model model) {
		model.addAllAttributes(pSvc.getMapData(arrId));
		return "Date";
	}
	
	
}
