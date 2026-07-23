package com.nh.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nh.service.BoardService;
import com.nh.service.PlaceService;

@PropertySource("classpath:secret.properties")
@Configuration
@Controller
public class NewPlanController {
	@Value("${google.api.key}")
    private String googleApiKey;

	@Autowired
	PlaceService pSvc;
	@Autowired
	BoardService bSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(NewPlanController.class);
	
	@RequestMapping("/newplan/arr")
	public String arr(Model model, HttpSession session) {
		
		List<Map<String, Object>> recommendedPlace = bSvc.viewRecommendedPlace();
		model.addAttribute("recommendedPlace",recommendedPlace);
		model.addAttribute("googleApiKey", googleApiKey);
		return "ArrPlace";
	}
	
	@RequestMapping("/newplan/start")
	public String start(Model model) {
		model.addAttribute("googleApiKey", googleApiKey);
		return "StartPlace";
	}
	
	@RequestMapping("/newplan/date")
	public String date(@RequestParam String arrId, Model model) {
		model.addAllAttributes(pSvc.getMapData(arrId));
		return "Date";
	}
	
	
}
