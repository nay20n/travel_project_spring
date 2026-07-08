package com.nh.TravelPlanner;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nh.dao.BlockDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class BlockDaoTest {
	@Autowired
	BlockDao blDao;
	
	// 블럭 삽입 테스트
	@Test
	@Transactional
	public void testAddBlock() {
		// 1) Given
		int bno = 1;
		String startTime = "2026-07-08 10:30";
		String endTime = "2026-07-08 10:40";
		
		// 2) When
		int blockIdx = blDao.addBlock(bno, startTime, endTime);
		
		// 3) Then : sql 오류가 없다면 성공
		System.out.println(blockIdx);
	}
	
	// 블럭 장소 수정 테스트
	@Test
	@Transactional
	public void testModifyBlockPlace() {
		// 1) Given
		int bno = 1;
		String placeId = "ChIJe1QMOBvraDUR9__2s01MxDE";
		
		// 2) When
		blDao.modifyBlockPlace(bno, placeId);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 블럭 삭제 테스트1
	@Test
	@Transactional
	public void testDelBlock1() {
		// 1) Given
		int blockIdx = 1;
		
		// 2) When
		blDao.deleteBlock(blockIdx);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 블럭 삭제 테스트2
	@Test
	@Transactional
	public void testDelBlock2() {
		// 1) Given
		int bno = 1;
		int memberId = 1;
		
		// 2) When
		blDao.deleteBlock(bno, memberId);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 블럭 색 수정 테스트
	@Test
	@Transactional
	public void testModifyBlockColor() {
		// 1) Given
		int blockIdx = 1;
		int colorIdx = 2;
		
		// 2) When
		blDao.modifyBlockColor(blockIdx, colorIdx);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 블럭 시간 수정 테스트
	@Test
	@Transactional
	public void testModifyBlockTime() {
		// 1) Given
		int blockIdx = 1;
		String startTime = "2026-07-08 11:50";
		String endTime = "2026-07-08 11:55";
		
		// 2) When
		blDao.modifyBlockTime(blockIdx, startTime, endTime);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 블럭 AI 반영여부 수정 테스트
	@Test
	@Transactional
	public void testModifyBlockCheckedAi() {
		// 1) Given
		int blockIdx = 1;
		boolean isCheckedAi = true;
		
		// 2) When
		blDao.modifyBlockCheckedAi(blockIdx, isCheckedAi);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 블럭 이동시간 수정 테스트
	@Test
	@Transactional
	public void testModifyBlockTravelTime() {
		// 1) Given
		int blockIdx = 1;
		int travelTime = 10;
		
		// 2) When
		blDao.modifyBlockTravelTime(blockIdx, travelTime);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 블록 팝업창 정보 조회 테스트
	@Test
	public void testGetBlockDetail() {
		// 1) Given
		int blockIdx = 1;
		
		// 2) When
		Map<String, Object> map1 = blDao.getBlockDetail(blockIdx);
		
		// 3) Then
		assertNotNull("1번 블록 정보는 널이 아님", map1);
		System.out.println(map1);
	}
	
	// 게시글 블럭 전체 조회
	@Test
	public void testGetAllblocks() {
		// 1) Given
		int bno = 1;
		
		// 2) When
		List<Map<String, Object>> listMap = blDao.getAllBlocks(bno);
		
		// 3) Then
		assertNotNull("리스트 널이 아님", listMap);
		for(int i=0;i<listMap.size();i++) {
			System.out.println(listMap.get(i));
		}
	}
	
	// 게시글 블럭 특정 시간 사이 조회 테스트
	@Test
	public void testGetBlocksBetween() {
		// 1) Given
		int bno = 1;
		String startTime = "2026-05-07";
		String endTime = "2026-05-08";
		
		// 2) When
		List<Map<String, Object>> listMap = blDao.getBlocksBetween(bno, startTime, endTime);
		
		// 3) Then
		assertNotNull("리스트 널이 아님", listMap);
		for(int i=0;i<listMap.size();i++) {
			System.out.println(listMap.get(i));
		}
	}
	
	// 하루 치 일정 삭제 테스트
	@Test
	@Transactional
	public void testDeleteBlockInDate() {
		// 1) Given
		int bno = 1;
		String date = "20260507";
		
		// 2) When
		blDao.deleteBlockInDate(bno, date);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 블럭 색 조회 테스트
	@Test
	public void testGetColors() {
		// 1) Given
		
		// 2) When
		List<Map<String,Object>> listColor = blDao.getColors();
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listColor);
		for(int i=0;i<listColor.size();i++) {
			System.out.println(listColor.get(i));
		}
	}
	
	// 블럭 복제 테스트
	@Test
	@Transactional
	public void testCopyBlock() {
		// 1) Given
		int newBno = 155; // 155번 게시글 없으면 오류나요!
		int bno = 1;
		String newStartTime = "2026-07-08";
		
		// 2) When
		blDao.copyBlock(newBno, bno, newStartTime);
		
		// 3) Then
		List<Map<String,Object>> listBlocks = blDao.getAllBlocks(newBno);
		assertNotNull("복제한 블럭은 조회되어야함", listBlocks);
	}
}