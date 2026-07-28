package com.nh.service;

import java.util.List;
import java.util.Map;

public interface ExternalApiService {
	/**
	 * 경로 문자열 배열 얻기
	 * @param travelMode 이동수단
	 * @param placeIds 장소아이디들
	 * @return api 응답 성공 시: routes.polyline.encodedPolyline / 실패 시: "fail"
	 */
	String getRoute(String travelMode, List<String> placeIds);
	
	/**
	 * ai 견적 확인
	 * @param bno
	 * @return transportCost, foodCost, roomCost, etcCost, maxCost
	 */
	Map<String,Object> searchCost(int bno);
	
	/**
	 * ai 일정 추천 및 db에 없는 장소 삽입
	 * @param userBlocks 하루 일정
	 * @param bno
	 * @param arrPlaceCity
	 * @return 추천 하루 일정
	 */
	List<Map<String,Object>> searchAiRecommend(List<Map<String, Object>> userBlocks, int bno, String arrPlaceCity);
}
