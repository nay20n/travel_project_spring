package com.nh.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardDao {
	@Autowired
	SqlSession sqlSession;
	
	// 도착 정보 조회
	@Override
	public String getArrPlaceIdByBno(int bno) {
		return sqlSession.selectOne("boardMapper.selectArrPlaceId", bno);
	}

	// 장소 이미지 조회
	@Override
	public List<String> getPlaceImages(String placeId) {
		return sqlSession.selectList("boardMapper.selectPlaceImages", placeId);
	}
	
	// 게시글 삽입
	@Override
	public void insertBoard(Map<String, Object> map1) {
		sqlSession.insert("boardMapper.insertBoard", map1);
	}
		
	// 게시글 찜 삽입
	@Override
	public void insertLikeBoard(Map<String, Object> map1) {
		sqlSession.insert("boardMapper.insertLikeBoard", map1);
	}
	
	// 게시글 찜 삭제
	@Override
	public void deleteLikeBoard(Map<String, Object> map1) {
		sqlSession.delete("boardMapper.deleteLikeBoard", map1);
	}
	
	// 일정에 들어간 장소 조회
	@Override
	public List<Map<String, Object>> getSelectedPlaces(Map<String, Object> map1) {
		return sqlSession.selectList("boardMapper.selectedPlaces", map1);
	}

	// 찜한 장소 조회
	@Override
	public List<Map<String, Object>> getLikedPlaces(Map<String, Object> map1) {
		return sqlSession.selectList("boardMapper.likedPlaces", map1);
	}
	
	// 게시글 제목 수정
	@Override
	public void modifyTilte(Map<String, Object> map1) {
		sqlSession.update("boardMapper.updateTilte", map1);
	}
	
	// 장소 검색 조회
	@Override
	public List<Map<String, Object>> getSerchedPlace(Map<String, Object> map1) {
		return sqlSession.selectList("boardMapper.selectSerchedPlace", map1);
	}
	
	// 공유 버튼 클릭 시 인증코드 및 만료일시 생성
	@Override
	public void createShareKey(Map<String, Object> map1) {
		sqlSession.update("boardMapper.updateShareKey", map1);
	}
	
	// 인증코드를 입력해서 공유회원이 들어온 경우
	@Override
	public void addSharedMember(Map<String, Object> map1) {
		sqlSession.update("boardMapper.updateSharedMember", map1);
	}
	
	// 일정 날짜 추가 및 삭제
	@Override
	public void modifyTravelDate(Map<String, Object> map1) {
		sqlSession.update("boardMapper.updateTravelDate", map1);		
	}
	
	// 최종 수정일 업데이트
	@Override
	public void modifyFinalDate(int bno) {
		sqlSession.update("boardMapper.updateFinalDate", bno);
	}

	// 게시글 정보 조회
	@Override
	public List<Map<String, Object>> getBoardInfo(Map<String, Object> map1) {
		return sqlSession.selectList("boardMapper.selectBoardInfo", map1);
	}
	
	// 타인의 일정 게시글 복제
	@Override
	public void copyBoard(Map<String, Object> map1) {
		sqlSession.insert("boardMapper.insertOtherBoard", map1);
	}
	
	// 최신 게시글 조회
	@Override
	public List<Map<String, Object>> getBoardsLatestOrder(Map<String, Object> map1) {
		if(map1.containsKey("memberId"))
			return sqlSession.selectList("boardMapper.selectBoardsLatestOrder", map1);
		return sqlSession.selectList("boardMapper.selectBoardsLatestOrderLogoutVer",map1);
	}
	
	// 검색어 게시글 조회
	@Override
	public List<Map<String, Object>> getBoardsKeyOrder(Map<String, Object> map1) {
		if(map1.containsKey("memberId"))
			return sqlSession.selectList("boardMapper.selectBoardsKeyOrder", map1);
		return sqlSession.selectList("boardMapper.selectBoardsKeyOrderLogoutVer",map1);
	}
	
	// 위도 경도 조회
	@Override
	public List<Map<String, Object>> getMapInfo(int bno) {
		return sqlSession.selectList("boardMapper.selectMapInfo", bno);
	}
	
	// 경비 수정
	@Override
	public void modifyCost(Map<String, Object> map1) {
		if(map1.containsKey("maxCost")) {
			sqlSession.update("boardMapper.updateAllCost", map1);
			return;
		}
		if(map1.containsKey("transportCost"))
			sqlSession.update("boardMapper.updateTransportCost", map1);
		if(map1.containsKey("foodCost"))
			sqlSession.update("boardMapper.updateFoodCost", map1);
		if(map1.containsKey("roomCost"))
			sqlSession.update("boardMapper.updateRoomCost", map1);
		if(map1.containsKey("etcCost"))
			sqlSession.update("boardMapper.updateEtcCost", map1);
	}

	// 추천 여행지 조회
	@Override
	public List<Map<String, Object>> viewRecommendedPlace() {
		return sqlSession.selectList("boardMapper.selectRecommendedPlace");
	}

	// 게시글 삭제
	@Override
	public void deleteBoard(int bno) {
		sqlSession.delete("boardMapper.deleteBoard", bno);
	}
}
