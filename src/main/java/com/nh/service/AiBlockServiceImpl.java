package com.nh.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.dao.AiBlockDao;
import com.nh.dao.BlockDao;

@Service
public class AiBlockServiceImpl implements AiBlockService {
	@Autowired
	AiBlockDao aDao;
	@Autowired
	BlockDao bDao;
	@Autowired
	AiBlockDao blDao;

	@Override
	public List<Map<String, Object>> getAiBlock(int bno) {
		return aDao.getAiBlock(bno);
	}

	@Override
	public void deleteAiBlock(int bno) {
		aDao.deleteAiBlock(bno);
	}

	@Override
	public void insertAiBlock(int bno, String placeId, String startTime, String endTime, int travelTime){
		//aDao.deleteAiBlock(bno); // 삭제 
		aDao.insertAiBlock(bno, placeId, startTime, endTime, travelTime);
	}

	@Override
	public List<Map<String, Object>> copyAiBlock(int bno) {
		bDao.deleteBlockByAiBlock(bno);
		aDao.copyAiBlock(bno);
		List<Map<String, Object>> blocks = blDao.getAiBlock(bno);
		return blocks;
	}

	@Override
	public List<Map<String, Object>> getAiChecked(int bno) {
		return aDao.getAiChecked(bno);
	}
	
}