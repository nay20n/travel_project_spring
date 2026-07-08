package com.nh.dao;

import java.util.List;
import java.util.Map;

public interface BoardDao {
	/**
	 * 게시글 삽입
	 * @param map1(writerId, startPlaceId, arrPlaceId, startDate, endDate, arrPlaceCity)
	 */
	void insertBoard(Map<String, Object> map1);
	
	/**
	 * 게시글 찜 삽입
	 * @param map1(memberId, bno)
	 */
	void insertLikeBoard(Map<String, Object> map1);
	
	/**
	 * 게시글 찜 삭제
	 * @param map1(memberId, bno)
	 */
	void deleteLikeBoard(Map<String, Object> map1);
	
	/**
	 * 일정에 들어간 장소 조회 (리스트가 비어있을 수 있음)
	 * @param map1(bno, memberId, start(시작번호), end(끝번호))
	 * @return 일정에 들어간 장소 map 리스트(placeId, name, category, address, lat, lng, avgRating, reviewCnt, isLiked)
	 */
	List<Map<String, Object>> getSelectedPlaces(Map<String, Object> map1);
	
	/**
	 * 찜한 장소 조회 (리스트가 비어있을 수 있음/최근에 찜한 순/20키로 이내이거나 행정구역이 같은 곳 조회)
	 * @param map1(bno, memberId, arrPlaceId, start(시작번호), end(끝번호))
	 * @return 찜한 장소 map 리스트(placeId, name, category, address, lat, lng, avgRating, reviewCnt, isLiked)
	 */
	List<Map<String, Object>> getLikedPlaces(Map<String, Object> map1);
	
	/**
	 * 게시글 제목 수정
	 * @param map1(title, bno)
	 */
	void modifyTilte(Map<String, Object> map1);
	
	/**
	 * 장소 검색 조회
	 * @param map1(memberId, bno, arrPlaceId, input, start(시작번호), end(끝번호))
	 * @return 검색한 장소 map 리스트(placeId, name, category, address, lat, lng, avgRating, reviewCnt, isLiked)
	 */
	List<Map<String, Object>> getSerchedPlace(Map<String, Object> map1);
	
	/**
	 * 공유 버튼 클릭 시 인증코드 및 만료일시 생성
	 * @param map1(key, bno)
	 */
	void createShareKey(Map<String, Object> map1);
	
	/**
	 * 인증코드를 입력해서 공유회원이 들어온 경우
	 * @param map1(memberId, bno, key)
	 */
	void addSharedMember(Map<String, Object> map1);

	/**
	 * 일정 날짜 추가 및 삭제
	 * @param map1(bno, startDate, endDate)
	 */
	void modifyTravelDate(Map<String, Object> map1);
	
	/**
	 * 최종 수정일 업데이트
	 * @param bno
	 */
	void modifyFinalDate(int bno);
	
	/**
	 * 게시글 정보 조회
	 * @param map1(memberId, bno)
	 * @return 해당 글의 정보 map 리스트(블럭 때문에)(writerId, title, startPlaceId, arrPlaceId, startDate, endDate, maxCost, transportCost, foodCost, roomCost, etcCost, reviewCnt, isLiked, likedCnt, blockIdx, startTime, endTime, colorIdx, colorCode, name)
	 */
	List<Map<String, Object>> getBoardInfo(Map<String, Object> map1);
	
	/**
	 * 타인의 일정 게시글 복제
	 * @param map1(memberId, startPlaceId, startDate, bno)
	 */
	int copyBoard(Map<String, Object> map1);

	/**
	 * 최신 게시글 조회(지도 그리기 때문에 장소가 들어간 블록이 있는 게시글만 조회됨/위도 경도는 페이징 처리를 위해 따로 조회)
	 * @param map1(memberId(선택적), start(시작번호), end(끝번호))
	 * @return 최신 게시글 map 리스트(bno, title, year, month, elapsedTime, likedCnt, isLiked)
	 */
	List<Map<String, Object>> getBoardsLastestOrder(Map<String, Object> map1);
	
	/**
	 * 검색어 게시글 조회(지도 그리기 때문에 장소가 들어간 블록이 있는 게시글만 조회됨/위도 경도는 페이징 처리를 위해 따로 조회)
	 * @param map1(memberId(선택적), input(검색어), start(시작번호), end(끝번호))
	 * @return 최신 게시글 map 리스트(bno, title, year, month, elapsedTime, likedCnt, isLiked)
	 */
	List<Map<String, Object>> getBoardsKeyOrder(Map<String, Object> map1);
	
	/**
	 * 위도 경도 조회
	 * @param bno
	 * @return 해당 게시글의 장소 위도 경도 리스트(lat, lng)
	 */
	List<Map<String, Object>> getMapInfo(int bno);
	
	/**
	 * 경비 수정
	 * @param map1(bno(필수 그 외 수정항목 별 업데이트), maxCost, transportCost, foodCost, roomCost, etcCost)
	 */
	void modifyCost(Map<String, Object> map1);
	
	/**
	 * 추천 여행지 조회
	 * @return 추천 여행지 리스트(spot(이름), arrPlaceId)
	 */
	List<Map<String, Object>> viewRecommendedPlace();
	
	/**
	 * 게시글 삭제(FK 연결되어 있을 시 삭제 불가)
	 * @param bno
	 */
	void deleteBoard(int bno);
	
	/**
	 * 글 삭제로 인한 찜한 게시글 삭제
	 * @param bno
	 */
	void deleteLikeBoardByBno(int bno);
}