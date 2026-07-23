package com.nh.controller;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nh.service.BlockService;
import com.nh.service.BoardService;
import com.nh.service.CommentService;
import com.nh.service.MemberService;
import com.nh.service.PlaceService;

@RestController
public class RestController {
	@Autowired
	CommentService cSvc;
	@Autowired
	BoardService bSvc;
	@Autowired
	PlaceService pSvc;
	@Autowired
	MemberService mSvc;
	@Autowired
	BlockService blSvc;
	
	// 시간 형식 변환 메서드
	public static String changeDateFormat(String input) {
		OffsetDateTime odt = OffsetDateTime.parse(input);
        ZonedDateTime kstDateTime = odt.atZoneSameInstant(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    
	    return kstDateTime.format(formatter);
	}
	// 블럭 장소 변경
	@PostMapping("/modifyBlockPlace")
	public String modifyBlockPlace(@RequestBody Map<String,Object> mapReq) {
		int blockIdx = (Integer)mapReq.get("blockIdx");
		String placeId = (String)mapReq.get("placeId");
		blSvc.modifyBlockPlace(blockIdx, placeId);
		return "블럭 변경";
	}
	// 블럭 색 변경
	@PostMapping("/modifyBlockColor")
	public String modifyBlockColor(@RequestBody Map<String,Object> mapReq) {
		int blockIdx = (Integer)mapReq.get("blockIdx");
		int colorIdx = (Integer)mapReq.get("colorIdx");
		blSvc.modifyBlockColor(blockIdx, colorIdx);
		return "블럭 색 변경";
	}
	// 블럭 시간 변경
	@PostMapping("/modifyBlockTime")
	public String modifyBlockTime(@RequestBody Map<String,Object> mapReq) {
		int blockIdx = (Integer)mapReq.get("blockIdx");
		String startTime = changeDateFormat((String)mapReq.get("startTime"));
		String endTime = changeDateFormat((String)mapReq.get("endTime"));
		blSvc.modifyBlockTime(blockIdx, startTime, endTime);
		return "블럭 변경";
	}
	// 블럭 삭제
	@PostMapping("/deleteBlock")
	public String deleteBlock(@RequestBody Map<String,Object> mapReq) {
		int blockIdx = (Integer)mapReq.get("blockIdx");
		blSvc.deleteBlock(blockIdx);
		return "블럭 삭제";
	}
	// 블럭 삽입
	@PostMapping("/addBlock")
	public int addBlock(@RequestBody Map<String,Object> mapReq) {
		int bno = (Integer)mapReq.get("bno");
		String startTime = changeDateFormat((String)mapReq.get("startTime"));
		String endTime = changeDateFormat((String)mapReq.get("endTime"));
		System.out.println(startTime+","+endTime);
		return blSvc.addBlock(bno, startTime, endTime);
	}
	// 게시글에 들어간 블럭 전체 조회
	@PostMapping("/getAllBlocks")
	public List<Map<String,Object>> getAllBlocks(@RequestBody Map<String,Object> mapReq) {
		int bno = (Integer)mapReq.get("bno");
		return blSvc.getAllBlocks(bno);
	}
	// 블럭 정보 팝업
	@PostMapping("/getBlockDetail")
	public Map<String,Object> getBlockDetail(@RequestBody Map<String,Object> mapReq) {
		int blockIdx = (Integer)mapReq.get("blockIdx");
		return blSvc.getBlockDetail(blockIdx);
	}
	// 블럭 색 가져오기
	@PostMapping("/getColors")
	public List<Map<String,Object>> getColors() {
		return blSvc.getColors();
	}
	// 게시글 최신순 조회
	@PostMapping("/getBoardsLastestOrder")
	public List<Map<String,Object>> getBoardsLastestOrder(@RequestBody Map<String,Object> mapReq, HttpSession session) {
		int pageNum = (Integer)mapReq.get("pageNum");
		return bSvc.getBoardsLastestOrder((Integer)session.getAttribute("loginId"), pageNum);
	}
	// 게시글 검색 조회
	@PostMapping("/getBoardsKeyOrder")
	public List<Map<String,Object>> getBoardsKeyOrder(@RequestBody Map<String,Object> mapReq, HttpSession session) {
		String input = (String)mapReq.get("input");
		int pageNum = (Integer)mapReq.get("pageNum");
		return bSvc.getBoardsKeyOrder((Integer)session.getAttribute("loginId"), input, pageNum);
	}
	// 게시글 삽입하기
	@PostMapping("/insertBoard")
	public int insertBoard(@RequestBody Map<String,Object> mapReq, HttpSession session){
		String arrId = (String)mapReq.get("arrId");
		String startId = (String)mapReq.get("startId");
		String startDate = (String)mapReq.get("startDate");
		String endDate = (String)mapReq.get("endDate");
		
		int loginId = (int)session.getAttribute("loginId");;
		String tempBno = (String)mapReq.get("bno");
		
		int bno;
		
		if(tempBno.equals("undefined")) {
			bno = bSvc.insertBoard(loginId, startId, arrId, startDate, endDate);
		} else {
			int copyBno = Integer.parseInt(tempBno);
			bno = bSvc.copyBoard(loginId, startId, startDate, copyBno);
		}
		
		//System.out.println(bno);
		return bno;
	}
	// 게시글 삭제하기
	@PostMapping("/deleteBoard")
	public String deleteBoard(@RequestBody Map<String,Object> mapReq, HttpSession session){
		int bno = Integer.parseInt((String)mapReq.get("bno"));

		// 게시글 삭제
		bSvc.deleteBoard(bno, (int)session.getAttribute("loginId"));
		return "게시글 삭제";
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
	public Map<String,Object> getBoardComment(@RequestBody Map<String,Object> mapReq, HttpSession session){
		int bno = Integer.parseInt((String)mapReq.get("bno"));
		int pageNum = (int)mapReq.get("pageNum");
		Map<String,Object> map1 = cSvc.getComment(bno, pageNum);
		
		// 로그인한 아이디와 작성자 아이디 비교를 위해 필요
		map1.put("loginId", (int)session.getAttribute("loginId"));
		return map1;
	}
	// 게시글 댓글 수정하기
	@PostMapping("/updateComment")
	public String updateComment(@RequestBody Map<String,Object> mapReq){
		int cno = (int)mapReq.get("cno");
		String content = (String)mapReq.get("content");
		cSvc.modifyComment(cno, content);
		return "update";
	}
	// 게시글 댓글 삽입하기
	@PostMapping("/insertComment")
	public String insertComment(@RequestBody Map<String,Object> mapReq, HttpSession session){
		String content = (String)mapReq.get("content");
		int bno = Integer.parseInt((String)mapReq.get("bno"));
		cSvc.insertComment(bno, (int)session.getAttribute("loginId"), content);
		return "insert";
	}
	// 게시글 댓글 삭제하기
	@PostMapping("/deleteComment")
	public String deleteComment(@RequestBody Map<String,Object> mapReq, HttpSession session){
		int cno = (int)mapReq.get("cno");
		cSvc.deleteComment(cno, (int)session.getAttribute("loginId"));
		return "delete";
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
	// 장소 검색 조회 
	@PostMapping("/getSerchedPlace")
	public List<Map<String,Object>> getSerchedPlace(@RequestBody Map<String,Object> mapReq, HttpSession session){
		int loginId = (int)session.getAttribute("loginId");
		int bno = (Integer)mapReq.get("bno");
		int pageNum = (Integer)mapReq.get("pageNum");
		String input = (String)mapReq.get("input");
		List<Map<String, Object>> listPlaces = bSvc.getSerchedPlace(loginId, bno, input, pageNum);
		
		return listPlaces;
	}
	// 내 일정에 들어간 장소 조회 
	@PostMapping("/getSelectedPlaces")
	public List<Map<String,Object>> getSelectedPlaces(@RequestBody Map<String,Object> mapReq, HttpSession session){
		int loginId = (int)session.getAttribute("loginId");
		int bno = (Integer)mapReq.get("bno");
		int pageNum = (Integer)mapReq.get("pageNum");
		List<Map<String, Object>> listPlaces = bSvc.getSelectedPlaces(bno,loginId,pageNum);
		
		return listPlaces;
	}
	// 찜한 장소 조회 
	@PostMapping("/getLikedPlaces")
	public List<Map<String,Object>> getLikedPlaces(@RequestBody Map<String,Object> mapReq, HttpSession session){
		int loginId = (int)session.getAttribute("loginId");
		int bno = (Integer)mapReq.get("bno");
		int pageNum = (Integer)mapReq.get("pageNum");
		List<Map<String, Object>> listPlaces = bSvc.getLikedPlaces(bno, loginId, pageNum);
		
		return listPlaces;
	}
	
	
	// 장소 정보 조회 (장소 정보창 팝업)
	@PostMapping("/getPlaceDetail")
	public Map<String,Object> getPlaceDetail(HttpSession session, String placeId) {
		int loginId = (int)session.getAttribute("loginId");
		int page = 1;
		
		Map<String,Object> placeDetail = pSvc.getPlaceDetail(placeId, loginId);
		List<Map<String,Object>> reviews = pSvc.getReviews(placeId, page);
		for(int i=0; i<reviews.size();i++) {
			reviews.get(i).put("finalDate",(reviews.get(0).get("finalDate") + "").substring(0, 16));
		}
		
		Map<String, Object> ret = new HashMap<>();
		ret.put("placeDetail", placeDetail);
		ret.put("reviews", reviews);
		ret.put("loginId", loginId);
		return ret;
	}
	// 장소 팝업 리뷰 조회 (장소 정보창 팝업)
	@PostMapping("/getReviews")
	public Map<String,Object> getReviews(@RequestBody Map<String,Object> mapReq,HttpSession session){
		int loginId = (int)session.getAttribute("loginId");
		int pageNum = (Integer)mapReq.get("pageNum");
		String placeId = (String)mapReq.get("placeId");
		List<Map<String,Object>> reviews = pSvc.getReviews(placeId, pageNum);
		for(int i=0; i<reviews.size();i++) {
			reviews.get(i).put("finalDate",(reviews.get(0).get("finalDate") + "").substring(0, 16));
		}
		
		Map<String, Object> ret = new HashMap<>();
		ret.put("reviews",reviews);
		ret.put("loginId", loginId);
		return ret;
	}
	
	// 내 게시글 조회 (마이페이지)
	@PostMapping("/getMyBoard")
	public List<Map<String,Object>> getMyBoard(HttpSession session, int pageNum) {
		int loginId = (int)session.getAttribute("loginId");
		List<Map<String,Object>> boardList = mSvc.getMyBoard(loginId, pageNum);
		return boardList;
	}
	// 내가 찜한 게시글 조회 (마이페이지)
	@PostMapping("/getLikedBoard")
	public List<Map<String,Object>> getLikedBoard(HttpSession session, int pageNum) {
		int loginId = (int)session.getAttribute("loginId");
		List<Map<String,Object>> boardList = mSvc.getLikedBoard(loginId, pageNum);
		return boardList;
	}
	// 내가 댓글을 단 게시글 조회 (마이페이지)
	@PostMapping("/getCommentBoard")
	public List<Map<String,Object>> getCommentBoard(HttpSession session, int pageNum) {
		int loginId = (int)session.getAttribute("loginId");
		List<Map<String,Object>> boardList = mSvc.getCommentBoard(loginId, pageNum);
		return boardList;
	}
	// 장소 댓글 입력 
	@PostMapping("/addReview")
	public String addReview(@RequestBody Map<String,Object> mapReq, HttpSession session) {
		int loginId = (int)session.getAttribute("loginId");
		String placeId = (String)mapReq.get("placeId");
		String content = (String)mapReq.get("content");
		int rating = (Integer)mapReq.get("rating");
		String image = (String)mapReq.get("image");
		pSvc.addReview(loginId, placeId, content, rating, image);
		return "insert";
	}
	// 장소 댓글 삭제 
	@PostMapping("/deleteReview")
	public String deleteReview(HttpSession session, int reviewIdx) {
		int loginId = (int)session.getAttribute("loginId");
		pSvc.deleteReview(reviewIdx , loginId);
		return "delete";
	}
	// 게시글 공유 링크 생성
	@PostMapping("/createShareKey")
	public String createShareKey() {
		return bSvc.createShareKey(0);
	}
	// 일정 날짜 추가 및 삭제 (main/month)
	@PostMapping("/modifyTravelDate")
	public Map<String,String> modifyTravelDate(@RequestBody Map<String,Object> mapReq) {
		int bno = (Integer)mapReq.get("bno");
		String startDate = (String)mapReq.get("startDate");
		String endDate = (String)mapReq.get("endDate");
		
		bSvc.modifyTravelDate(bno, startDate, endDate);
		
		Map<String, String> ret = new HashMap<>();
		ret.put("startDate", startDate);
		ret.put("endDate", endDate);
		
		return ret;
	}
}
