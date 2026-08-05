package com.nh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nh.dao.BlockDao;

@Service
public class BlockServiceImpl implements BlockService {
	@Autowired
	BlockDao blDao;
	
	// 블럭 삽입
	@Override
	@Transactional
	public Map<String,Object> addBlock(int bno, String startTime, String endTime) {
		int blockIdx = blDao.addBlock(bno, startTime, endTime);
		List<Map<String,Object>> blocks = blDao.getAllBlocks(bno);
		
		Map<String,Object> ret = new HashMap<>();
		ret.put("blockIdx", blockIdx);
		ret.put("blocks", blocks);
		
		return ret;
	}

	// 블럭 장소 수정 후 블럭들 리턴
	@Override
	@Transactional
	public List<Map<String, Object>> modifyBlockPlace(int blockIdx, String placeId, int bno) {
		blDao.modifyBlockPlace(blockIdx, placeId);
		List<Map<String, Object>> blocks = blDao.getAllBlocks(bno);
		//System.out.println("장소 수정 서비스의 블럭 상태" + blocks);
		return blocks;
	}
	
	// 블럭 삭제
	@Override
	@Transactional
	public List<Map<String,Object>> deleteBlock(int blockIdx, int bno) {
		blDao.deleteBlock(blockIdx);
		List<Map<String,Object>> blocks = blDao.getAllBlocks(bno);
		return blocks;
	}
	
	// 블럭 색 수정
	@Override
	@Transactional
	public List<Map<String,Object>> modifyBlockColor(int blockIdx, int colorIdx, int bno) {
		blDao.modifyBlockColor(blockIdx, colorIdx);
		List<Map<String,Object>> blocks = blDao.getAllBlocks(bno);
		return blocks;
	}
	
	// 블럭 시간 수정
	@Override
	@Transactional
	public List<Map<String,Object>> modifyBlockTime(int blockIdx, String startTime, String endTime, int bno) {
		blDao.modifyBlockTime(blockIdx, startTime, endTime);
		List<Map<String,Object>> blocks = blDao.getAllBlocks(bno);
		return blocks;
	}
	
	// 블럭 ai 반영 유무 수정
	@Override
	public void modifyBlockCheckedAi(int blockIdx, boolean isCheckedAi) {
		blDao.modifyBlockCheckedAi(blockIdx, isCheckedAi);
	}
	
	// 블럭 이동시간 수정
	@Override
	public void modifyBlockTravelTime(int blockIdx, int travelTime) {
		blDao.modifyBlockTravelTime(blockIdx, travelTime);
	}
	
	// 블럭 팝업
	@Override
	public Map<String, Object> getBlockDetail(int blockIdx) {
		return blDao.getBlockDetail(blockIdx);
	}
	
	// 게시글 블럭 조회
	@Override
	public List<Map<String, Object>> getAllBlocks(int bno) {
		return blDao.getAllBlocks(bno);
	}
	
	// 특정 시간 사이 블럭 조회
	@Override
	public List<Map<String, Object>> getBlocksBetween(int bno, String inputStartTime, String inputEndTime) {
		return blDao.getBlocksBetween(bno, inputStartTime, inputEndTime);
	}
	
	// 블럭 색 변경 팝업
	@Override
	public List<Map<String, Object>> getColors() {
		return blDao.getColors();
	}

	// 구글 Routes용 블럭 조회
	@Override
	public List<String> getBlocksForRoutes(int bno, String inputTime) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", bno);
		map1.put("inputTime", inputTime);
		return blDao.getBlocksForRoutes(bno, inputTime);
	}
}
