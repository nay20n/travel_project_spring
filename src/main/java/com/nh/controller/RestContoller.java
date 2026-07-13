package com.nh.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nh.service.BoardService;

@RestController
public class RestContoller {
	
	@Autowired
	BoardService bSvc;
	
	// 게시글 삽입하기
	@PostMapping("/insertBoard")
	public int insertBoard(@RequestBody Map<String,Object> mapReq){
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
	@RequestMapping("/getSelectedPlaces")
	public Map<String,Object> getSelectedPlaces(HttpSession session, Model model) {
		//임시 로그인
		int loginId = 1;
		session.setAttribute("loginId", loginId);
		int page = 1;
		
		//List<Map<String,Object>> list = bSvc.getSelectedPlaces(bno, loginId, page);
		return null;
	}
	@PostMapping("/updateBoardTitle")
	public void updateBoardTilte(@RequestBody Map<String,Object> mapReq, HttpServletRequest request){
		// 요청에서 제목, bno 가져오기
		String title = (String)mapReq.get("title");
		int bno = Integer.parseInt((String)mapReq.get("bno"));
		
		// 제목 없데이트
		bSvc.modifyTilte(title, bno);
	}
	// 게시글 찜하기
	@PostMapping("/insertLikeBoard")
	public boolean insertLikeBoard(HttpSession session, int bno) {
		//임시 로그인
		int loginId = 1;
		session.setAttribute("loginId", loginId);
		try {
	        bSvc.insertLikeBoard(1, bno);
	        return true; 
	    } catch (Exception e) {
	        return false;    
	    }
	}
	//게시글 찜 삭제하기 
	@PostMapping("/deleteLikeBoard")
	public boolean deleteLikeBoard(HttpSession session, int bno) {
		//임시 로그인,
		int loginId = 1;
		session.setAttribute("loginId", loginId);
		try {
	        bSvc.deleteLikeBoard(1, bno);
	        return true; 
	    } catch (Exception e) {
	        return false;    
	    }
	}
}
