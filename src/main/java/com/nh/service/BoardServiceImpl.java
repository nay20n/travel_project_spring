package com.nh.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.dao.AiBlockDao;
import com.nh.dao.BlockDao;
import com.nh.dao.BoardDao;
import com.nh.dao.CommentDao;
import com.nh.dao.PlaceDao;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	BoardDao bDao;
	@Autowired
	PlaceDao pDao;
	@Autowired
	BlockDao blDao;
	@Autowired
	AiBlockDao aDao;
	@Autowired
	CommentDao cDao;
	
	// 게시글 삽입
	@Override
	public void insertBoard(int writerId, String startPlaceId, String arrPlaceId, String startDate, String endDate, String arrPlaceCity) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("writerId", writerId);
		map1.put("startPlaceId", startPlaceId);
		map1.put("arrPlaceId", arrPlaceId);
		map1.put("startDate", startDate);
		map1.put("endDate", endDate);
		map1.put("arrPlaceCity", arrPlaceCity);
		
		bDao.insertBoard(map1);
	}
	
	// 게시글 찜 삽입
	@Override
	public void insertLikeBoard(int memberId, int bno) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", memberId);
		map1.put("bno", bno);
		
		bDao.insertLikeBoard(map1);
	}

	// 게시글 찜 삭제
	@Override
	public void deleteLikeBoard(int memberId, int bno) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", memberId);
		map1.put("bno", bno);
		
		bDao.deleteLikeBoard(map1);
	}
	
	// 일정에 들어간 장소 조회
	@Override
	public List<Map<String, Object>> getSelectedPlaces(int bno, int memberId, int page) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("memberId", memberId);
		
		// 페이지네이션 (10개씩 조회)
		map1.put("end", page*10);
		map1.put("start", page*10-9);
		
		List<Map<String, Object>> listPlaces = bDao.getSelectedPlaces(map1);
		
		// 장소 이미지 추가
		for(int i=0;i<listPlaces.size();i++) {
			Map<String, Object> tempMap = listPlaces.get(i);
			String placeId = (String)tempMap.get("placeId");
			tempMap.put("images", pDao.getPlaceImages(placeId));
		}
		
		return listPlaces;
	}
	
	// 찜한 장소 조회
	@Override
	public List<Map<String, Object>> getLikedPlaces(int bno, int memberId, int page) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("memberId", memberId);
		
		// 페이지네이션 (10개씩 조회)
		map1.put("end", page*10);
		map1.put("start", page*10-9);
		
		// 도착 정보
		map1.put("arrPlaceId", pDao.getArrPlaceIdByBno(bno));

		List<Map<String, Object>> listPlaces = bDao.getLikedPlaces(map1);
		
		// 장소 이미지 추가
		for(int i=0;i<listPlaces.size();i++) {
			Map<String, Object> tempMap = listPlaces.get(i);
			String placeId = (String)tempMap.get("placeId");
			tempMap.put("images", pDao.getPlaceImages(placeId));
		}
		
		return listPlaces;
	}
	
	// 게시글 제목 수정
	@Override
	public void modifyTilte(String title, int bno) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("title", title);
		map1.put("bno", bno);
		
		bDao.modifyTilte(map1);
	}

	// 장소 검색 조회
	@Override
	public List<Map<String, Object>> getSerchedPlace(int memberId, int bno, String input, int page) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", memberId);
		map1.put("bno", bno);
		
		// 도착 정보
		map1.put("arrPlaceId", pDao.getArrPlaceIdByBno(bno));
		
		// 페이지네이션 (10개씩 조회)
		map1.put("end", page*10);
		map1.put("start", page*10-9);
		
		// %검색%
		map1.put("input", "%" + input + "%");
		
		List<Map<String, Object>> listPlaces = bDao.getSerchedPlace(map1);
		
		// 장소 이미지 추가
		for(int i=0;i<listPlaces.size();i++) {
			Map<String, Object> tempMap = listPlaces.get(i);
			String placeId = (String)tempMap.get("placeId");
			tempMap.put("images", pDao.getPlaceImages(placeId));
		}
		return listPlaces;
	}
	
	// 인증코드 및 만표일시 생성
	@Override
	public String createShareKey(int bno) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		
		// 랜덤키
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		map1.put("key", sb.toString());
		
		bDao.createShareKey(map1);
		return (String)map1.get("key");
	}

	// 인증코드를 입력해서 공유회원이 들어온 경우
	@Override
	public void addSharedMember(int memberId, int bno, String key) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("key", key);
		map1.put("bno", bno);
		map1.put("memberId", memberId);
		
		bDao.addSharedMember(map1);
	}
	
	// 일정 날짜 추가 및 삭제
	@Override
	public void modifyTravelDate(int bno, String startDate, String endDate) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("startDate", startDate);
		map1.put("endDate", endDate);
		
		bDao.modifyTravelDate(map1);
	}
	
	// 최종 수정일 업데이트
	@Override
	public void modifyFinalDate(int bno) {
		bDao.modifyFinalDate(bno);
	}

	// 게시글 정보 조회
	@Override
	public Map<String, Object> getBoardInfo(int memberId, int bno) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("memberId", memberId);
		
		Map<String, Object> mapBoard = bDao.getBoardInfo(map1);
		mapBoard.put("blocks", blDao.getAllBlocks(bno));
		
		return mapBoard;
	}
	
	// 타인의 일정 게시글 복제
	@Override
	public void copyBoard(int memberId, String startPlaceId, String startDate, int bno) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", memberId);
		map1.put("startPlaceId", startPlaceId);
		map1.put("startDate", startDate);
		map1.put("bno", bno);
		
		// 블럭 복제
		int newBno = bDao.copyBoard(map1);
		blDao.copyBlock(newBno, bno, startDate);
	}
	
	// 최신 게시글 조회
	@Override
	public List<Map<String, Object>> getBoardsLastestOrder(Integer memberId, int page) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", memberId);
		
		// 페이지네이션 (8개씩 조회)
		map1.put("end", page*8);
		map1.put("start", page*8-7);
		
		List<Map<String, Object>> listBoard = bDao.getBoardsLastestOrder(map1);
		
		// mapData 추가
		for(int i=0;i<listBoard.size();i++) {
			Map<String, Object> tempBoard = listBoard.get(i);
			int bno = ((BigDecimal)tempBoard.get("bno")).intValue();
			tempBoard.put("mapData", bDao.getMapInfo(bno));
		}
		
		return listBoard;
	}

	// 검색어 게시글 조회
	@Override
	public List<Map<String, Object>> getBoardsKeyOrder(Integer memberId, String input, int page) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", memberId);
		
		// 페이지네이션 (8개씩 조회)
		map1.put("end", page*8);
		map1.put("start", page*8-7);
		
		// %검색%
		map1.put("input", "%" + input + "%");
		
		List<Map<String, Object>> listBoard = bDao.getBoardsKeyOrder(map1);
		
		// mapData 추가
		for(int i=0;i<listBoard.size();i++) {
			Map<String, Object> tempBoard = listBoard.get(i);
			int bno = ((BigDecimal)tempBoard.get("bno")).intValue();
			tempBoard.put("mapData", bDao.getMapInfo(bno));
		}
		
		return listBoard;
	}

	// 경비 수정
	@Override
	public void modifyCost(int bno, Integer maxCost, Integer transportCost, Integer foodCost, Integer roomCost,
			Integer etcCost) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		if(maxCost!=null) map1.put("maxCost", maxCost);
		if(transportCost!=null) map1.put("transportCost", transportCost);
		if(foodCost!=null) map1.put("foodCost", foodCost);
		if(roomCost!=null) map1.put("roomCost", roomCost);
		if(etcCost!=null) map1.put("etcCost", etcCost);
		
		bDao.modifyCost(map1);
	}

	// 추천 여행지 조회
	@Override
	public List<Map<String, Object>> viewRecommendedPlace() {
		return bDao.viewRecommendedPlace();
	}

	// 게시글 삭제
	@Override
	public void deleteBoard(int bno, int memberId) {
		// 찜 삭제
		bDao.deleteLikeBoardByBno(bno);
		// ai 블럭 삭제
		aDao.deleteAiBlock(bno);
		// 블럭 삭제
		blDao.deleteBlock(bno, memberId);
		// 댓글 삭제
		cDao.deleteCommentByBno(bno);
		// 게시글 삭제
		bDao.deleteBoard(bno);
	}
}