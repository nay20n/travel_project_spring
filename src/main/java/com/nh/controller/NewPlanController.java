package com.nh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NewPlanController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(NewPlanController.class);
	
	@RequestMapping("/newplan/arr")
	public String arr() {
		return "ArrPlace";
	}
	
	@RequestMapping("/newplan/start")
	public String start() {
		return "StartPlace";
	}
	
	@RequestMapping("/newplan/date")
	public String date() {
		return "Date";
	}
	
	
}
