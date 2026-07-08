package com.nh.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AiBlockDaoImpl implements AiBlockDao{
	@Autowired
	SqlSession sqlSession;
	
	@Override
	public List<Map<String, Object>> getAiBlock(int bno) {
		return sqlSession.selectList("AiBlockMapper.getAiBlock", bno);
	}
	
	@Override
	public void deleteAiBlock(int bno) {
		sqlSession.delete("AiBlockMapper.deleteAiBlock", bno);
	}

	@Override
	public void insertAiBlock(int bno, String placeId, String startTime, String endTime, int travelTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("bno", bno);
		map.put("placeId", placeId);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("travelTime", travelTime);
		
		sqlSession.insert("AiBlockMapper.insertAiBlock", map);
	}

	@Override
	public void copyAiBlock(int bno) {
		sqlSession.insert("AiBlockMapper.copyAiBlock", bno);
	}

}
