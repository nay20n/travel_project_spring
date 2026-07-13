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
import com.nh.service.CommentService;

@RestController
public class RestContoller {
	@Autowired
	CommentService cSvc;
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
	// 일정에 포함된 장소 가져오기(필터)
	@RequestMapping("/getSelectedPlaces")
	public Map<String,Object> getSelectedPlaces(HttpSession session, Model model) {
		//임시 로그인
		int loginId = 1;
		session.setAttribute("loginId", loginId);
		int page = 1;
		
		//List<Map<String,Object>> list = bSvc.getSelectedPlaces(bno, loginId, page);
		return null;
	}
	// 게시글 제목 수정하기
	@PostMapping("/updateBoardTitle")
	public String updateBoardTilte(@RequestBody Map<String,Object> mapReq){
		// 요청에서 제목, bno 가져오기
		String title = (String)mapReq.get("title");
		int bno = Integer.parseInt((String)mapReq.get("bno"));
		
		// 제목 업데이트
		bSvc.modifyTilte(title, bno);
		return "게시글 제목 변경됨";
	}
	// 게시글 댓글 가져오기
	@PostMapping("/getBoardComment")
	public Map<String,Object> updateBoardTilte(Integer pageNum, Integer bno){
		if(pageNum==null) pageNum=1;
		if(bno==null) bno=1;
		return cSvc.getComment(bno, pageNum);
	}
	// 게시글 예산 수정하기
	@PostMapping("/updateBoardFee")
	public String updateBoardFee(@RequestBody Map<String,Object> mapReq){
		int bno = Integer.parseInt((String)mapReq.get("bno"));
		Integer currentFee = Integer.parseInt((String)mapReq.get("fee"));
		String field = (String)mapReq.get("field");
		
		// 사용자 예산 수정
		if(field.equals("transportCost"))
			bSvc.modifyCost(bno, null, currentFee, null, null, null);
		if(field.equals("foodCost"))
			bSvc.modifyCost(bno, null, null, currentFee, null, null);
		if(field.equals("roomCost"))
			bSvc.modifyCost(bno, null, null, null, currentFee, null);
		if(field.equals("etcCost"))
			bSvc.modifyCost(bno, null, null, null, null, currentFee);
		// ai 예상 견적(미완성)
		if(field.equals("all"))
			bSvc.modifyCost(bno, null, null, null, null, null);
		
		return "게시글 예산 수정됨";
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
