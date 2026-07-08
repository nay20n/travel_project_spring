package com.nh.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@GetMapping("/plan/{bno}")
	public String planDetail(@PathVariable int bno, Model model) {
		return "Board";
	}
	
	@GetMapping("/plan/{bno}/week")
	public String planDetailWeek(@PathVariable int bno, Model model) {
		return "MainWeek";
	}
	
	@GetMapping("/plan/{bno}/month")
	public String planDetailMonth(@PathVariable int bno, Model model) {
		return "MainMonth";
	}
	
	@GetMapping("/plan/{bno}/day")
	public String planDetailDay(@PathVariable int bno, Model model) {
		return "MainDay";
	}
}
