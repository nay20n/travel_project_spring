package com.nh.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceDaoImpl implements PlaceDao {
	@Autowired
	SqlSession sqlSession;
	
	// 도착 정보 조회
	@Override
	public String getArrPlaceIdByBno(int bno) {
		return sqlSession.selectOne("placeMapper.selectArrPlaceId", bno);
	}

	// 장소 이미지 조회
	@Override
	public List<String> getPlaceImages(String placeId) {
		return sqlSession.selectList("placeMapper.selectPlaceImages", placeId);
	}
	
	// 장소 정보 조회
	@Override
	public Map<String, Object> viewPlaceDetails(String placeId, int memberId) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("placeId", placeId);
		map1.put("memberId", memberId);
		
		return sqlSession.selectOne("placeMapper.selectPlaceDetails", map1);
	}
	
	// 찜한 장소 삽입
	@Override
	public void addLikedPlace(int memberId, String placeId) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("placeId", placeId);
		map1.put("memberId", memberId);
		
		sqlSession.insert("placeMapper.insertLikedPlace", map1);
	}
	
	// 찜한 장소 삭제
	@Override
	public void deleteLikedPlace(int memberId, String placeId) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("placeId", placeId);
		map1.put("memberId", memberId);
		
		sqlSession.delete("placeMapper.deleteLikedPlace", map1);
	}
	
	// 장소 댓글 목록 조회
	@Override
	public List<Map<String, Object>> getReviews(String placeId, int start, int end) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("placeId", placeId);
		map1.put("start", start);
		map1.put("end", end);
		return sqlSession.selectList("placeMapper.selectReviews", map1);
	}
	
	// 장소 댓글 삽입
	@Override
	public void addReview(int memberId, String placeId, String content, int rating, String image) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("placeId", placeId);
		map1.put("memberId", memberId);
		map1.put("content", content);
		map1.put("rating", rating);
		map1.put("image", image==null ? "" : image);
		
		sqlSession.insert("placeMapper.insertReview", map1);
	}
	
	// 장소 댓글 수정
	@Override
	public void modifyReview(int reviewIdx, int memberId, String content, int rating, String image) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("reviewIdx", reviewIdx);
		map1.put("memberId", memberId);
		map1.put("content", content);
		map1.put("rating", rating);
		map1.put("image", image==null ? "" : image);
		
		sqlSession.update("placeMapper.updateReview", map1);
	}
	
	// 장소 댓글 삭제
	@Override
	public void deleteReview(int reviewIdx, int memberId) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("reviewIdx", reviewIdx);
		map1.put("memberId", memberId);

		sqlSession.delete("placeMapper.deleteReview", map1);
	}
	
	// 장소 삽입
	@Override
	public void addPlace(String placeId, String name, String category, String address, double lat, double lng,
			String websiteUrl, String businessHours) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("placeId", placeId);
		map1.put("name", name);
		map1.put("category", category);
		map1.put("address", address);
		map1.put("lat", lat);
		map1.put("lng", lng);
		map1.put("websiteUrl", websiteUrl==null ? "" : websiteUrl);
		map1.put("businessHours", businessHours==null ? "" : businessHours);
		sqlSession.insert("placeMapper.insertPlace", map1);
	}
	
	// DB에 있는 장소인지 확인
	@Override
	public boolean isExistPlace(String placeId) {
		Integer cnt = sqlSession.selectOne("placeMapper.selectIsExistPlace", placeId);
		return cnt>0;
	}
}
