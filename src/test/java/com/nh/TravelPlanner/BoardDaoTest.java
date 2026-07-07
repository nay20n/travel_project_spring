package com.nh.TravelPlanner;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nh.dao.BoardDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class BoardDaoTest {
	@Autowired
	BoardDao bDao;
	
	// 게시글 삽입 테스트
	@Test
	@Transactional
	public void testInsertBoard() {
		// 1) Given : 게시글에 필수로 들어가는 정보
		Map<String, Object> map1 = new HashMap<>();
		map1.put("writerId", 2);
		map1.put("startPlaceId", "ChIJA3CU42aifDURaq-3csGXvuc");
		map1.put("arrPlaceId", "ChIJFaft2p-NaDURpWUIUUDNxCE");
		map1.put("startDate", "20240404");
		map1.put("endDate", "20240406");
		map1.put("arrPlaceCity", "부산");
		
		// 2) When
		bDao.insertBoard(map1);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 게시글 찜 삽입 테스트
	@Test
	@Transactional
	public void testInsertLikeBoard() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", 2);
		map1.put("bno", 2);
		
		// 2) When
		bDao.insertLikeBoard(map1);;
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 게시글 찜 삭제 테스트
	@Test
	@Transactional
	public void testDeleteLikeBoard() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", 1);
		map1.put("bno", 2);
		
		// 2) When
		bDao.deleteLikeBoard(map1);;
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 일정에 들어간 장소 조회 테스트
	@Test
	public void testGetSelectedPlaces() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", 1);
		map1.put("bno", 1);
		map1.put("start", 1); // 시작번호
		map1.put("end", 3); // 끝번호
		
		// 2) When
		List<Map<String, Object>> listPlaceMap = bDao.getSelectedPlaces(map1);
		
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
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", 1);
		map1.put("bno", 1);
		map1.put("arrPlaceId", "ChIJFaft2p-NaDURpWUIUUDNxCE");
		map1.put("start", 1); // 시작번호
		map1.put("end", 3); // 끝번호
		
		// 2) When
		List<Map<String, Object>> listPlaceMap = bDao.getLikedPlaces(map1);
		
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
		Map<String, Object> map1 = new HashMap<>();
		map1.put("title", "수정한 제목");
		map1.put("bno", 1);
		
		// 2) When
		bDao.modifyTilte(map1);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 장소 검색 조회 테스트
	@Test
	public void testGetSerchedPlace() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", 1);
		map1.put("bno", 1);
		map1.put("arrPlaceId", "ChIJFaft2p-NaDURpWUIUUDNxCE");
		map1.put("input", "%" + "부산" + "%");
		map1.put("start", 1); // 시작번호
		map1.put("end", 3); // 끝번호
		
		// 2) When
		List<Map<String,Object>> listPlaceMap = bDao.getSerchedPlace(map1);
		
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
		Map<String, Object> map1 = new HashMap<>();
		// 랜덤키
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		map1.put("key", sb.toString());
		map1.put("bno", 1);
		
		// 2) When
		bDao.createShareKey(map1);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 인증코드를 입력해서 공유회원이 들어온 경우 테스트
	@Test
	@Transactional
	public void testAddSharedMember() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("key", "잘못된키");
		map1.put("bno", 1);
		map1.put("memberId", 2);
		
		// 2) When
		bDao.addSharedMember(map1);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 일정 날짜 추가 및 삭제 테스트
	@Test
	@Transactional
	public void testModifyTravelDate() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", 1);
		map1.put("startDate", "20260706");
		map1.put("endDate", "20260708");
		
		// 2) When
		bDao.modifyTravelDate(map1);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 최종 수정일 업데이트 테스트
	@Test
	@Transactional
	public void testModifyFinalDate() {
		// 1) Given
		int bno = 1;
		
		// 2) When
		bDao.modifyFinalDate(bno);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 게시글 정보 조회 테스트
	@Test
	public void testGetBoardInfo() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", 1);
		map1.put("memberId", 1);
		
		// 2) When
		List<Map<String, Object>> listMap = bDao.getBoardInfo(map1);
		
		// 3) Then
		assertNotNull("1번 게시글 정보는 널이 아님", listMap);
		
		for(int i=0;i<listMap.size();i++) {
			System.out.println(listMap.get(i));
		}
	}
	
	// 타인의 일정 게시글 복제 테스트
	@Test
	@Transactional
	public void testCopyBoard() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", 4);
		map1.put("startPlaceId", "ChIJA3CU42aifDURaq-3csGXvuc");
		map1.put("startDate", "20270404");
		map1.put("bno", 1);
		
		// 2) When
		bDao.copyBoard(map1);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 최신 게시글 조회 테스트
	@Test
	public void testGetBoardsLastestOrder() {
		// 1) Given
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", 1); // null 넣을 수 있음
		map1.put("start", 1); // 첫번째 게시글부터
		map1.put("end", 2); // 두번째 게시글까지
		
		// 2) When
		List<Map<String, Object>> listMap = bDao.getBoardsLastestOrder(map1);
		
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
		Map<String, Object> map1 = new HashMap<>();
		map1.put("memberId", 1); // null 넣을 수 있음
		map1.put("input", "%" + "부산" + "%");
		map1.put("start", 1); // 첫번째 게시글부터
		map1.put("end", 2); // 두번째 게시글까지
		
		// 2) When
		List<Map<String, Object>> listMap = bDao.getBoardsKeyOrder(map1);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listMap);
		
		for(int i=0;i<listMap.size();i++) {
			System.out.println(listMap.get(i));
		}
	}
	
	// 위도 경도 조회 테스트
	@Test
	public void testGetMapInfo() {
		// 1) Given
		int bno = 1;
		
		// 2) When
		List<Map<String, Object>> listMap = bDao.getMapInfo(bno);
		
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
		Map<String, Object> map1 = new HashMap<>();
		map1.put("bno", 1);
		//map1.put("maxCost", 1000); // 선택적
		map1.put("transportCost", 1000); // 선택적
		map1.put("foodCost", 1000); // 선택적
		map1.put("roomCost", 1000); // 선택적
		map1.put("etcCost", 1000); // 선택적
		
		// 2) When
		bDao.modifyCost(map1);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 추천 여행지 조회 테스트
	@Test
	public void testViewRecommendedPlace() {
		// 1) Given
		
		// 2) When
		List<Map<String, Object>> listMap = bDao.viewRecommendedPlace();
		
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
		int bno = 155; // 블럭 없는 게시글이어야해서 일단 있는 거 넣었어요. 155 없으면 에러날 거예요.
		
		// 2) When
		bDao.deleteBoard(bno);
		
		// 3) Then
	}
}