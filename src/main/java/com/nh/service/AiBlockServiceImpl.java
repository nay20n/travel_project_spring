package com.nh.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.dao.AiBlockDao;

@Service
public class AiBlockServiceImpl implements AiBlockService {
	@Autowired
	AiBlockDao aDao;

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
		aDao.deleteAiBlock(bno); // 삭제 
		aDao.insertAiBlock(bno, placeId, startTime, endTime, travelTime);
	}

	@Override
	public void copyAiBlock(int bno) {
		aDao.copyAiBlock(bno);
	}
	
}