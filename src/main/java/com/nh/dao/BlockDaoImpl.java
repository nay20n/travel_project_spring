package com.nh.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BlockDaoImpl implements BlockDao {
	@Autowired
	SqlSession sqlSession;
	
	// 블럭 삽입
	@Override
	public int addBlock(int bno, String startTime, String endTime) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("startTime", startTime);
		map1.put("endTime", endTime);
		
		sqlSession.insert("blockMapper.insertBlock", map1);
		return (int)map1.get("blockIdx");
	}
	
	// 블럭 장소 수정
	@Override
	public void modifyBlockPlace(int blockIdx, String placeId) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("blockIdx", blockIdx);
		map1.put("placeId", placeId);
		
		sqlSession.update("blockMapper.updateBlockPlace", map1);
	}
	
	// 인덱스로 블럭 삭제
	@Override
	public void deleteBlock(int blockIdx) {
		sqlSession.delete("blockMapper.deleteBlockByIdx", blockIdx);
	}

	// 게시글 번호로 블럭 삭제
	@Override
	public void deleteBlock(int bno, int memberId) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("memberId", memberId);
		
		sqlSession.delete("blockMapper.deleteBlockByBno", map1);
	}
	
	// 블럭 색 수정
	@Override
	public void modifyBlockColor(int blockIdx, int colorIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("blockIdx", blockIdx);
		map1.put("colorIdx", colorIdx);
		
		sqlSession.update("blockMapper.updateBlockColor", map1);
	}
	
	// 블럭 시간 수정
	@Override
	public void modifyBlockTime(int blockIdx, String startTime, String endTime) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("blockIdx", blockIdx);
		map1.put("startTime", startTime);
		map1.put("endTime", endTime);
		
		sqlSession.update("blockMapper.updateBlockTime", map1);
	}
	
	// 블럭 AI 반영여부 수정
	@Override
	public void modifyBlockCheckedAi(int blockIdx, boolean isCheckedAi) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("blockIdx", blockIdx);
		map1.put("checkedAi", isCheckedAi ? 1 : 0);
		
		sqlSession.update("blockMapper.updateBlockCheckedAi", map1);
	}
	
	// 블럭 이동시간 수정
	@Override
	public void modifyBlockTravelTime(int blockIdx, int travelTime) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("blockIdx", blockIdx);
		map1.put("travelTime", travelTime);
		
		sqlSession.update("blockMapper.updateBlockTravelTime", map1);
	}

	// 블록 팝업창 정보 조회
	@Override
	public Map<String, Object> getBlockDetail(int blockIdx) {
		return sqlSession.selectOne("blockMapper.selectBlockDetail", blockIdx);
	}
	
	// 게시글 블럭 전체 조회
	@Override
	public List<Map<String, Object>> getAllBlocks(int bno) {
		return sqlSession.selectList("blockMapper.selectAllBlocks", bno);
	}
	
	// 게시글 블록 특정 시간 사이 조회
	@Override
	public List<Map<String, Object>> getBlocksBetween(int bno, String inputStartTime, String inputEndTime) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("inputStartTime", inputStartTime);
		map1.put("inputEndTime", inputEndTime);
		
		return sqlSession.selectList("blockMapper.selectBlocksBetween", map1);
	}

	// 하루 치 일정 삭제
	@Override
	public void deleteBlockInDate(int bno, String date) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("date", date);
		
		sqlSession.delete("blockMapper.deleteBlockInDate", map1);
	}

	// 블럭 색 조회
	@Override
	public List<Map<String, Object>> getColors() {
		return sqlSession.selectList("blockMapper.selectColors");
	}
	
	// 블럭 복제
	@Override
	public void copyBlock(int newBno, int bno, String newStartTime) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("newBno", newBno);
		map1.put("bno", bno);
		map1.put("newStartTime", newStartTime);
		
		sqlSession.insert("blockMapper.insertCopyBlock", map1);
	}
}
