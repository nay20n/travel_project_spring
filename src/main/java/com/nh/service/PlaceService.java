package com.nh.service;

import java.util.List;
import java.util.Map;

public interface PlaceService {
	/**
	 * 장소 위도 경도 조회
	 * @param placeId
	 * @return lat lng
	 */
	Map<String, Object> getMapData(String placeId);
	
	/**
	 * 장소 정보 조회(장소 정보창 팝업)
	 * @param placeId
	 * @param memberId
	 * @return name, avgRating, category, reviewCnt, address, businessHours, websiteUrl, isLiked, images 리스트, rating0~5
	 */
	Map<String, Object> getPlaceDetail(String placeId, int memberId);
	
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
	 * @param page
	 * @return 댓글 정보들 리스트(reviewIdx, content, rating, finalDate, image, nickName, profileImg)
	 */
	List<Map<String,Object>> getReviews(String placeId, int page);
	
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
	 * DB에 없는 장소 삽입(Place API)
	 * @param placeId
	 * @param name
	 * @param category
	 * @param address
	 * @param lat
	 * @param lng
	 * @param websiteUrl(null 가능)
	 * @param businessHours(null 가능)
	 */
	void addPlace(String placeId, String name, String category, String address, double lat, double lng, String websiteUrl, String businessHours, String imgs);
}
