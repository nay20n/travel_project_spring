package com.nh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.dao.BoardDao;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	BoardDao bDao;
	
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
			tempMap.put("images", bDao.getPlaceImages(placeId));
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
		map1.put("arrPlaceId", bDao.getArrPlaceIdByBno(bno));

		List<Map<String, Object>> listPlaces = bDao.getLikedPlaces(map1);
		
		// 장소 이미지 추가
		for(int i=0;i<listPlaces.size();i++) {
			Map<String, Object> tempMap = listPlaces.get(i);
			String placeId = (String)tempMap.get("placeId");
			tempMap.put("images", bDao.getPlaceImages(placeId));
		}
		
		return listPlaces;
	}
	
	// 게시글 제목 수정
	@Override
	public void modifyTilte(Map<String, Object> map1) {
		bDao.modifyTilte(map1);
	}

	// 장소 검색 조회
	@Override
	public List<Map<String, Object>> getSerchedPlace(Map<String, Object> map1) {
		// 페이지네이션 (10개씩 조회)
		int page = (int)map1.get("page");
		map1.put("end", page*10);
		map1.put("start", page*10-9);
		
		// %검색%
		map1.put("input", "%" + map1.get("input") + "%");
		
		List<Map<String, Object>> listPlaces = bDao.getSerchedPlace(map1);
		
		// 장소 이미지 추가
		for(int i=0;i<listPlaces.size();i++) {
			Map<String, Object> tempMap = listPlaces.get(i);
			String placeId = (String)tempMap.get("placeId");
			tempMap.put("images", bDao.getPlaceImages(placeId));
		}
		return listPlaces;
	}
	
	// 인증코드 및 만표일시 생성
	@Override
	public void createShareKey(int bno) {
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
	}
	
	
}
