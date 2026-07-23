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

import com.nh.service.BoardService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class BoardServiceTest {
	@Autowired
	BoardService bSvc;
	
	// 게시글 삽입 테스트
	@Test
	@Transactional
	public void testInsertBoard() {
		// 1) Given : 게시글에 필수로 들어가는 정보
		int writerId = 2;
		String startPlaceId = "ChIJA3CU42aifDURaq-3csGXvuc";
		String arrPlaceId = "ChIJFaft2p-NaDURpWUIUUDNxCE";
		String startDate = "20240404";
		String endDate = "20240406";
		
		// 2) When
		int bno = bSvc.insertBoard(writerId, startPlaceId, arrPlaceId, startDate, endDate);
		
		// 3) Then : sql 오류가 없다면 성공
		System.out.println(bno);
	}
	
	// 게시글 찜 삽입 테스트
	@Test
	@Transactional
	public void testInsertLikeBoard() {
		// 1) Given
		int memberId = 2;
		int bno = 2;
		
		// 2) When
		bSvc.insertLikeBoard(memberId, bno);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 게시글 찜 삭제 테스트
	@Test
	@Transactional
	public void testDeleteLikeBoard() {
		// 1) Given
		int memberId = 1;
		int bno = 2;
		
		// 2) When
		bSvc.deleteLikeBoard(memberId, bno);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 일정에 들어간 장소 조회 테스트
	@Test
	public void testGetSelectedPlaces() {
		// 1) Given
		int bno = 1;
		int memberId = 1;
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> listPlaceMap = bSvc.getSelectedPlaces(bno, memberId, page);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listPlaceMap);
		
		for(int i=0;i<listPlaceMap.size();i++) {
			System.out.println(listPlaceMap.get(i));
		}
	}
	
	// 찜한 장소 조회 테스트
	@Test
	public void testGetLikedPlaces() {
		// 1) Given
		int memberId = 1;
		int bno = 1;
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> listPlaceMap = bSvc.getLikedPlaces(bno, memberId, page);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listPlaceMap);
		
		for(int i=0;i<listPlaceMap.size();i++) {
			System.out.println(listPlaceMap.get(i));
		}
	}
	
	// 게시글 제목 수정 테스트
	@Test
	@Transactional
	public void testModifyTilte() {
		// 1) Given
		String title = "수정한 제목";
		int bno = 1;
		
		// 2) When
		bSvc.modifyTilte(title, bno);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 장소 검색 조회 테스트
	@Test
	public void testGetSerchedPlace() {
		// 1) Given
		int memberId = 1;
		int bno = 1;
		String input = "부산";
		int page = 1;
		
		// 2) When
		List<Map<String,Object>> listPlaceMap = bSvc.getSerchedPlace(memberId, bno, input, page);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listPlaceMap);
		
		for(int i=0;i<listPlaceMap.size();i++) {
			System.out.println(listPlaceMap.get(i));
		}
	}
	
	// 인증코드 및 만료일시 생성 테스트
	@Test
	@Transactional
	public void testCreateShareKey() {
		// 1) Given
		int bno = 1;
	
		// 2) When
		String key = bSvc.createShareKey(bno);
		
		// 3) Then
		assertNotNull("인증키는 null이 아님", key);
	}
	
	// 인증코드를 입력해서 공유회원이 들어온 경우 테스트
	@Test
	@Transactional
	public void testAddSharedMember() {
		// 1) Given
		String key = "잘못된 키";
		int bno = 1;
		int memberId = 1;
		
		// 2) When
		bSvc.addSharedMember(memberId, bno, key);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 일정 날짜 추가 및 삭제 테스트
	@Test
	@Transactional
	public void testModifyTravelDate() {
		// 1) Given
		int bno = 1;
		String startDate = "20260706";
		String endDate = "20260708";
		
		// 2) When
		bSvc.modifyTravelDate(bno, startDate, endDate);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 최종 수정일 업데이트 테스트
	@Test
	@Transactional
	public void testModifyFinalDate() {
		// 1) Given
		int bno = 1;
		
		// 2) When
		bSvc.modifyFinalDate(bno);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 게시글 정보 조회 테스트
	@Test
	public void testGetBoardInfo() {
		// 1) Given
		int bno = 1;
		int memberId = 1;
		
		// 2) When
		Map<String, Object> mapBoard = bSvc.getBoardInfo(memberId, bno);
		
		// 3) Then
		assertNotNull("1번 게시글 정보는 널이 아님", mapBoard);
		System.out.println(mapBoard);
	}
	
	// 타인의 일정 게시글 복제 테스트
	@Test
	@Transactional
	public void testCopyBoard() {
		// 1) Given
		int memberId = 4;
		String startPlaceId = "ChIJA3CU42aifDURaq-3csGXvuc";
		String startDate = "20270404";
		int bno = 1;
		
		// 2) When
		bSvc.copyBoard(memberId, startPlaceId, startDate, bno);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 최신 게시글 조회 테스트
	@Test
	public void testGetBoardsLastestOrder() {
		// 1) Given
		Integer memberId = 1; // 널 가능
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> listMap = bSvc.getBoardsLastestOrder(memberId, page);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listMap);
		
		for(int i=0;i<listMap.size();i++) {
			System.out.println(listMap.get(i));
		}
	}
	
	// 검색어 게시글 조회 테스트
	@Test
	public void testGetBoardsKeyOrder() {
		// 1) Given
		Integer memberId = null;
		String input = "부산";
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> listMap = bSvc.getBoardsKeyOrder(memberId, input, page);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listMap);
		
		for(int i=0;i<listMap.size();i++) {
			System.out.println(listMap.get(i));
		}
	}
	
	// 경비 수정 테스트
	@Test
	@Transactional
	public void testModifyCost() {
		// 1) Given
		int bno = 1;
		Integer maxCost = 1000; // null 가능
		Integer transportCost = 1000; // null 가능
		Integer foodCost = 1000; // null 가능
		Integer roomCost = 1000; // null 가능
		Integer etcCost = 1000; // null 가능
		
		// 2) When
		bSvc.modifyCost(bno, maxCost, transportCost, foodCost, roomCost, etcCost);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 추천 여행지 조회 테스트
	@Test
	public void testViewRecommendedPlace() {
		// 1) Given
		
		// 2) When
		List<Map<String, Object>> listMap = bSvc.viewRecommendedPlace();
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listMap);
		
		for(int i=0;i<listMap.size();i++) {
			System.out.println(listMap.get(i));
		}
	}
	
	// 게시글 삭제 테스트
	@Test
	@Transactional
	public void testDeleteBoard() {
		// 1) Given
		int bno = 1;
		int memberId = 1;
		
		// 2) When
		bSvc.deleteBoard(bno, memberId);
		
		// 3) Then
	}
	
	// 게시글 city로 조회한 장소 조회 테스트 
	@Test 
	public void testGetSerchedPlaceByCity() {
		// 1) Given
		int memberId = 1;
		int bno = 1;
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> listMap = bSvc.getSerchedPlaceByCity(memberId, bno,page);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listMap);
		
		for(int i=0;i<listMap.size();i++) {
			System.out.println(listMap.get(i));
		}
	}
}