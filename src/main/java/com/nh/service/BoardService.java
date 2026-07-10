package com.nh.service;

import java.util.List;
import java.util.Map;

public interface BoardService {
	/**
	 * 게시글 삽입
	 * @param writerId
	 * @param startPlaceId
	 * @param arrPlaceId
	 * @param startDate
	 * @param endDate
	 * @param arrPlaceCity
	 */
	void insertBoard(int writerId, String startPlaceId, String arrPlaceId, String startDate, String endDate, String arrPlaceCity);
	
	/**
	 * 게시글 찜 삽입
	 * @param memberId
	 * @param bno
	 */
	void insertLikeBoard(int memberId, int bno);
	
	/**
	 * 게시글 찜 삭제
	 * @param memberId
	 * @param bno
	 */
	void deleteLikeBoard(int memberId, int bno);
	
	/**
	 * 일정에 들어간 장소 조회 (리스트가 비어있을 수 있음/장소 이미지가 없을 경우 빈 리스트)
	 * @param bno
	 * @param memberId(찜 유무 표시를 위해 필요/공유일 경우 달라서)
	 * @param page
	 * @return 일정에 들어간 장소 map 리스트(placeId, name, category, address, lat, lng, avgRating, reviewCnt, isLiked, images리스트)
	 */
	List<Map<String, Object>> getSelectedPlaces(int bno, int memberId, int page);
	
	/**
	 * 찜한 장소 조회 (리스트가 비어있을 수 있음/최근에 찜한 순/20키로 이내이거나 행정구역이 같은 곳 조회)
	 * @param bno(반경 20키로 이내 설정을 위해 도착 정보를 받아야하므로)
	 * @param memberId(공유일 경우 id가 서로 다르므로)
	 * @param page
	 * @return 찜한 장소 map 리스트(placeId, name, category, address, lat, lng, avgRating, reviewCnt, isLiked, images리스트)
	 */
	List<Map<String, Object>> getLikedPlaces(int bno, int memberId, int page);
	
	/**
	 * 게시글 제목 수정
	 * @param title
	 * @param bno
	 */
	void modifyTilte(String title, int bno);
	
	/**
	 * 장소 검색 조회
	 * @param memberId
	 * @param bno
	 * @param input
	 * @param page
	 * @return 검색한 장소 map 리스트(placeId, name, category, address, lat, lng, avgRating, reviewCnt, isLiked, images리스트)
	 */
	List<Map<String, Object>> getSerchedPlace(int memberId, int bno, String input, int page);

	/**
	 * 공유 버튼 클릭 시 인증코드 및 만료일시 생성
	 * @param bno
	 * @return 인증키
	 */
	String createShareKey(int bno);
	
	/**
	 * 인증코드를 입력해서 공유회원이 들어온 경우
	 * @param memberId
	 * @param bno
	 * @param key
	 */
	void addSharedMember(int memberId, int bno, String key);

	/**
	 * 일정 날짜 추가 및 삭제
	 * @param bno
	 * @param startDate
	 * @param endDate
	 */
	void modifyTravelDate(int bno, String startDate, String endDate);
	
	/**
	 * 최종 수정일 업데이트
	 * @param bno
	 */
	void modifyFinalDate(int bno);

	/**
	 * 게시글 정보 조회
	 * @param memberId 로그인한 아이디
	 * @param bno
	 * @return 해당 글의 정보 map (bno, writerId, title, startPlaceId, arrPlaceId, startDate, endDate, arrPlaceCity, maxCost, transportCost, foodCost, roomCost, etcCost, reviewCnt, isLiked, likedCnt, blocks리스트(blockIdx, startTime, endTime, colorIdx, colorCode, name))
	 */
	Map<String, Object> getBoardInfo(int memberId, int bno);
	
	/**
	 * 타인의 일정 게시글 복제 (블럭도 같이 복제)
	 * @param memberId
	 * @param startPlaceId
	 * @param startDate
	 * @param bno
	 */
	void copyBoard(int memberId, String startPlaceId, String startDate, int bno);

	/**
	 * 최신 게시글 조회(지도 그리기 때문에 장소가 들어간 블록이 있는 게시글만 조회됨)
	 * @param memberId(비로그인 시 null)
	 * @param page
	 * @return 최신 게시글 map 리스트(bno, title, year, month, elapsedTime, likedCnt, isLiked, mapData리스트(키:lat,lng))
	 */
	List<Map<String, Object>> getBoardsLastestOrder(Integer memberId, int page);
	
	/**
	 * 검색어 게시글 조회(지도 그리기 때문에 장소가 들어간 블록이 있는 게시글만 조회됨/위도 경도는 페이징 처리를 위해 따로 조회)
	 * @param memberId(비로그인 시 null)
	 * @param input
	 * @param page
	 * @return 최신 게시글 map 리스트(bno, title, year, month, elapsedTime, likedCnt, isLiked, mapData리스트(키:lat,lng))
	 */
	List<Map<String, Object>> getBoardsKeyOrder(Integer memberId, String input, int page);
	
	/**
	 * 경비 수정
	 * @param bno
	 * @param maxCost(null 가능)
	 * @param transportCost(null 가능)
	 * @param foodCost(null 가능)
	 * @param roomCost(null 가능)
	 * @param etcCost(null 가능)
	 */
	void modifyCost(int bno, Integer maxCost, Integer transportCost, Integer foodCost, Integer roomCost, Integer etcCost);
	
	/**
	 * 추천 여행지 조회
	 * @return 추천 여행지 리스트(spot(이름), arrPlaceId)
	 */
	List<Map<String, Object>> viewRecommendedPlace();

	/**
	 * 게시글 삭제(블럭, 찜, ai블럭, 댓글도 같이 삭제됨)
	 * 다른 삭제들도 필요해서 오류 상태
	 * @param bno
	 * @param memberId
	 */
	void deleteBoard(int bno, int memberId);
}
