package com.nh.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nh.service.BoardService;

@RestController
public class RestContoller {
	
	@Autowired
	BoardService bSvc;
	
	@PostMapping("/DataInsertBoard")
	public int DataInsertBoard(@RequestBody Map<String,Object> mapReq){
		String arrId = (String)mapReq.get("arrId");
		String startId = (String)mapReq.get("startId");
		String startDate = (String)mapReq.get("startDate");
		String endDate = (String)mapReq.get("endDate");
		String arrCity = (String)mapReq.get("arrCity");
		int memberId = 1;
		
		int bno = bSvc.insertBoard(memberId, startId, arrId, startDate, endDate, arrCity);
		//System.out.println(bno);
		return bno;
	}
	@RequestMapping("/MainWeekGetSelectedPlaces")
	public Map<String,Object> MainWeekGetSelectedPlaces(HttpSession session, Model model) {
		//임시 로그인
		int loginId = 1;
		session.setAttribute("loginId", loginId);
		int page = 1;
		
		//List<Map<String,Object>> list = bSvc.getSelectedPlaces(bno, loginId, page);
		return null;
	}
	
}
