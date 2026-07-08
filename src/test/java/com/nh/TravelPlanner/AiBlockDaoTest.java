package com.nh.TravelPlanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nh.dao.AiBlockDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})


public class AiBlockDaoTest {
	
	@Autowired
	AiBlockDao aDao;
	
	//AI 추천 블럭 조회
	@Test
	public void testGetAiBlock() {
		// 1) Given
		int bno = 2;
		
		// 2) When
		List<Map<String,Object>> list = aDao.getAiBlock(bno);
		
		// 3) Then : 조회할 list null이면 안됨.
		assertNotNull("조회할 list가 null이면 안됨.", list);
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i).get("idx") + " / " + list.get(i).get("bno") + " / " + list.get(i).get("placeId"));
		}
	}
	
	// Ai 블럭 삭제 
	@Test
	@Transactional
	public void testDeleteAiBlock() {
		// 1) Given
		int bno = 1;
		
		// 2) When
		aDao.deleteAiBlock(bno);
		
		// 3) Then : 삭제한 (bno=2) 게시글이 null이여야함.
		List<Map<String, Object>> list = aDao.getAiBlock(bno);
		assertEquals("삭제한 블럭들이 존재하면 안됨.", list.size(), 0);
	}
	
	// Ai 블럭 삽입
	@Test
	@Transactional
	public void testInsertAiBlock() {
		// 1) Given
		int bno = 102;
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		String startTime = "2026-03-21 14:00:00";
		String endTime = "2026-03-21 14:00:00";
		int travelTime = 10;
		
		// 2) When
		aDao.insertAiBlock(bno, placeId, startTime, endTime, travelTime);
		
		// 3) Then : 삽입한 블럭이 존재해야함
		List<Map<String, Object>> list = aDao.getAiBlock(bno);
		assertNotNull("삽입한 블럭이 존재해야함", list);
	}
	
	// AI 블록 내 일정에 복제
	@Test
	@Transactional
	public void testCopyAiBlock() {
		// 1) Given
		int bno = 102;
		
		// 2) When
		aDao.copyAiBlock(bno);
		
		// 3) Then : sql문 오류 없으면 성공
		//copy한것들이 블럭에 들어가야됨.
	}
}
