package com.nh.dao;

import java.util.List;

public interface PlaceDao {
	/**
	 * 장소이미지 조회 (리스트가 비어있을 수 있음)
	 * @param placeId
	 * @return 장소이미지 리스트
	 */
	List<String> getPlaceImages(String placeId);
	
	/**
	 * 도착 정보 조회
	 * @param bno
	 * @return arrPlaceId
	 */
	String getArrPlaceIdByBno(int bno);
}