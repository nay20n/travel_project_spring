package com.nh.dao;

import java.util.List;
import java.util.Map;

public interface PlaceDao {
	/**
	 * 장소이미지 조회 (리스트가 비어있을 수 있음)
	 * @param placeId
	 * @return 장소이미지 리스트
	 */
	List<String> getPlaceImages(String placeId);
	
	/**
	 * 장소 대표 이미지 조회 (없을 수 있음)
	 * @param placeId
	 * @return 장소 대표 이미지
	 */
	String getPlaceImageOne(String placeId);
	
	/**
	 * 도착 정보 조회
	 * @param bno
	 * @return arrPlaceId
	 */
	String getArrPlaceIdByBno(int bno);
	
	/**
	 * 장소정보 조회 (이미지 조회가 분리되어 있으므로 꼭 넣어줘야함)
	 * @param placeId
	 * @param memberId
	 * @return 장소정보 name, avgRating, category, reviewCnt, address, businessHours, websiteUrl, isLiked, rating0~5
	 */
	Map<String, Object> viewPlaceDetails(String placeId, int memberId);

	/**
	 * 찜한장소 삽입
	 * @param memberId
	 * @param placeId
	 */
	void addLikedPlace(int memberId, String placeId);
	
	/**
	 * 찜한장소 삭제
	 * @param memberId
	 * @param placeId
	 */
	void deleteLikedPlace(int memberId, String placeId);
	
	/**
	 * 장소 댓글 목록 조회
	 * @param placeId
	 * @param start 시작번호
	 * @param end 끝번호
	 * @return 댓글 정보들 리스트(reviewIdx, content, rating, finalDate, image, nickName, profileImg)
	 */
	List<Map<String,Object>> getReviews(String placeId, int start, int end);
	
	/**
	 * 장소 댓글 삽입
	 * @param memberId
	 * @param placeId
	 * @param content
	 * @param rating
	 * @param image(null 가능)
	 */
	void addReview(int memberId, String placeId, String content, int rating, String image);
	
	/**
	 * 장소 댓글 수정
	 * @param reviewIdx
	 * @param memberId
	 * @param content
	 * @param rating
	 * @param image(null 가능)
	 */
	void modifyReview(int reviewIdx, int memberId, String content, int rating, String image);
	
	/**
	 * 장소 댓글 삭제
	 * @param reviewIdx
	 * @param memberId
	 */
	void deleteReview(int reviewIdx, int memberId);

	/**
	 * DB에 장소 삽입(Place API)
	 * @param placeId
	 * @param name
	 * @param category
	 * @param address
	 * @param lat
	 * @param lng
	 * @param websiteUrl(null 가능)
	 * @param businessHours(null 가능)
	 */
	void addPlace(String placeId, String name, String category, String address, double lat, double lng, String websiteUrl, String businessHours);

	/**
	 * DB에 있는 장소인지 확인
	 * @param placeId
	 * @return true=존재함
	 */
	boolean isExistPlace(String placeId);
}