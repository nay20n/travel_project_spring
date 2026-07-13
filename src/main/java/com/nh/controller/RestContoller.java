package com.nh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nh.service.BoardService;
import com.nh.service.CommentService;
import com.nh.service.MemberService;
import com.nh.service.PlaceService;

@RestController
public class RestContoller {
	@Autowired
	CommentService cSvc;
	@Autowired
	BoardService bSvc;
	@Autowired
	PlaceService pSvc;
	@Autowired
	MemberService mSvc;
	
	// 게시글 삽입하기
	@PostMapping("/insertBoard")
	public int insertBoard(@RequestBody Map<String,Object> mapReq, HttpSession session){
		String arrId = (String)mapReq.get("arrId");
		String startId = (String)mapReq.get("startId");
		String startDate = (String)mapReq.get("startDate");
		String endDate = (String)mapReq.get("endDate");
		String arrCity = (String)mapReq.get("arrCity");
		int loginId = (int)session.getAttribute("loginId");;
		
		int bno = bSvc.insertBoard(loginId, startId, arrId, startDate, endDate, arrCity);
		//System.out.println(bno);
		return bno;
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
	public Map<String,Object> getBoardComment(@RequestBody Map<String,Object> mapReq){
		int bno = Integer.parseInt((String)mapReq.get("bno"));
		int pageNum = (int)mapReq.get("pageNum");
		return cSvc.getComment(bno, pageNum);
	}
	// 게시글 댓글 수정하기
	@PostMapping("/updateComment")
	public String updateComment(@RequestBody Map<String,Object> mapReq){
		int cno = (int)mapReq.get("cno");
		String content = (String)mapReq.get("content");
		cSvc.modifyComment(cno, content);
		return "댓글 수정됨";
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
		int loginId = (int)session.getAttribute("loginId");
		try {
	        bSvc.insertLikeBoard(loginId, bno);
	        return true; 
	    } catch (Exception e) {
	        return false;    
	    }
	}
	// 게시글 찜 삭제하기 
	@PostMapping("/deleteLikeBoard")
	public boolean deleteLikeBoard(HttpSession session, int bno) {
		//임시 로그인,
		int loginId = (int)session.getAttribute("loginId");
		try {
	        bSvc.deleteLikeBoard(loginId, bno);
	        return true; 
	    } catch (Exception e) {
	        return false;    
	    }
	}
	// 장소 찜하기 
	@PostMapping("/addLikedPlace")
	public boolean addLikedPlace(HttpSession session, String placeId) {
		int loginId = (int)session.getAttribute("loginId");
		try {
	        pSvc.addLikedPlace(loginId, placeId);
	        return true; 
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return false;    
	    }
	}
	// 장소 찜 삭제하기
	@PostMapping("/deleteLikedPlace")
	public boolean deleteLikedPlace(HttpSession session, String placeId) {
		int loginId = (int)session.getAttribute("loginId");
		try {
	        pSvc.deleteLikedPlace(loginId, placeId);
	        return true; 
	    } catch (Exception e) {
	    e.printStackTrace();
	        return false;    
	    }
	}
	// 장소 정보 조회 (장소 정보창 팝업)
	@PostMapping("/getPlaceDetail")
	public Map<String,Object> getPlaceDetail(HttpSession session, String placeId) {
		int loginId = (int)session.getAttribute("loginId");
		int page = 1;
		List<Map<String,Object>> reviews = pSvc.getReviews(placeId, page);
		for(int i=0; i<reviews.size();i++) {
			reviews.get(i).put("finalDate",(reviews.get(0).get("finalDate") + "").substring(0, 16));
		}
		Map<String,Object> placeDetail = pSvc.getPlaceDetail(placeId, loginId);
		
		Map<String, Object> ret = new HashMap<>();
		ret.put("placeDetail", placeDetail);
		ret.put("reviews", reviews);
		return ret;
	}
	// 내 게시글 조회 (마이페이지)
	@PostMapping("/getMyBoard")
	public List<Map<String,Object>> getMyBoard(HttpSession session, int page) {
		int loginId = (int)session.getAttribute("loginId");
		List<Map<String,Object>> boardList = mSvc.getMyBoard(loginId, page);
		return boardList;
	}
	
}
