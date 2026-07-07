package com.nh.dao;

import java.util.List;

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
}
