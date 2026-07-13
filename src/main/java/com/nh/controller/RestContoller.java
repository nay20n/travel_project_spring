package com.nh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nh.service.BoardService;
import com.nh.service.PlaceService;

@RestController
public class RestContoller {
	
	@Autowired
	BoardService bSvc;
	@Autowired
	PlaceService pSvc;
	
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
		Map<String,Object> placeDetail = pSvc.getPlaceDetail(placeId, loginId);
		
		Map<String, Object> ret = new HashMap<>();
		ret.put("placeDetail", placeDetail);
		ret.put("reviews", reviews);
		return ret;
	}
	
}
