package com.nh.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.dao.BlockDao;

@Service
public class BlockServiceImpl implements BlockService {
	@Autowired
	BlockDao blDao;
	
	// 블럭 삽입
	@Override
	public int addBlock(int bno, String startTime, String endTime) {
		int blockIdx = blDao.addBlock(bno, startTime, endTime);
		return blockIdx;
	}

	// 블럭 장소 수정
	@Override
	public void modifyBlockPlace(int blockIdx, String placeId) {
		blDao.modifyBlockPlace(blockIdx, placeId);
	}
	
	// 블럭 삭제
	@Override
	public void deleteBlock(int blockIdx) {
		blDao.deleteBlock(blockIdx);
	}
	
	// 블럭 색 수정
	@Override
	public void modifyBlockColor(int blockIdx, int colorIdx) {
		blDao.modifyBlockColor(blockIdx, colorIdx);
	}
	
	// 블럭 시간 수정
	@Override
	public void modifyBlockTime(int blockIdx, String startTime, String endTime) {
		blDao.modifyBlockTime(blockIdx, startTime, endTime);
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
}
