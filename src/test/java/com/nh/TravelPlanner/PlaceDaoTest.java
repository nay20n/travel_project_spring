package com.nh.TravelPlanner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nh.dao.PlaceDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class PlaceDaoTest {
	@Autowired
	PlaceDao pDao;
	
	// 도착 정보 조회 테스트
	@Test
	public void testGetArrPlaceIdByBno() {
		// 1) Given
		int bno = 1;
		
		// 2) When
		String arrPlaceId = pDao.getArrPlaceIdByBno(bno);
		
		// 3) Then
		assertNotNull("도착정보는 널이 아님", arrPlaceId);
		
		System.out.println(arrPlaceId);
	}
	
	// 장소이미지 조회 테스트
	@Test
	public void testGetPlaceImages() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		
		// 2) When
		List<String> listimages = pDao.getPlaceImages(placeId);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listimages);
		
		System.out.println(listimages);
	}
	
	// 장소 대표이미지 조회 테스트
	@Test
	public void testGetPlaceImageOne() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		
		// 2) When
		String image = pDao.getPlaceImageOne(placeId);
		
		// 3) Then
		System.out.println(image);
	}
	
	// 장소 정보 조회 테스트
	@Test
	public void testViewPlaceDetails() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		int memberId = 1;
		
		// 2) When
		Map<String, Object> listDetail = pDao.viewPlaceDetails(placeId, memberId);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listDetail);
		
		System.out.println(listDetail);
	}
	
	// 찜한 장소 삽입 테스트
	@Test
	@Transactional
	public void testAddLikedPlace() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		int memberId = 3;
		
		// 2) When
		pDao.addLikedPlace(memberId, placeId);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 찜한 장소 삭제 테스트
	@Test
	@Transactional
	public void testDeleteLikedPlace() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		int memberId = 1;
		
		// 2) When
		pDao.deleteLikedPlace(memberId, placeId);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 장소 댓글 목록 조회 테스트
	@Test
	public void testViewReviews() {
		// 1) Given
		String placeId = "ChIJe1QMOBvraDUR9__2s01MxDE";
		int start = 1;
		int end = 3;
		
		// 2) When
		List<Map<String, Object>> listReview = pDao.getReviews(placeId, start, end);
		
		// 3) Then
		assertNotNull("리스트는 널이 아님", listReview);
		
		System.out.println(listReview);
	}
	
	// 장소 댓글 삽입 테스트
	@Test
	@Transactional
	public void testAddReview() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		int memberId = 1;
		String content = "테스트 내용";
		int rating = 3;
		String image = null;
		
		// 2) When
		pDao.addReview(memberId, placeId, content, rating, image);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 장소 댓글 수정 테스트
	@Test
	@Transactional
	public void testModifyReview() {
		// 1) Given
		int reviewIdx = 1;
		int memberId = 1;
		String content = "테스트 수정 내용";
		int rating = 3;
		String image = "수정";
		
		// 2) When
		pDao.modifyReview(reviewIdx, memberId, content, rating, image);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 장소 댓글 삭제 테스트
	@Test
	@Transactional
	public void testDeleteReview() {
		// 1) Given
		int reviewIdx = 1;
		int memberId = 1;
		
		// 2) When
		pDao.deleteReview(reviewIdx, memberId);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 장소 삽입 테스트
	@Test
	@Transactional
	public void testAddPlace() {
		// 1) Given
		String placeId = "테스트 아이디";
		String name = "테스트 장소 이름2";
		String category = "ex";
		String address = "테스트 주소";
		Double lat = 10.001;
		Double lng = 10.001;
		String websiteUrl = "테스트 링크";
		String businessHours = "테스트 영업시간";
		
		// 2) When
		pDao.addPlace(placeId, name, category, address, lat, lng, websiteUrl, businessHours);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 장소 확인 테스트
	@Test
	@Transactional
	public void testIsExistPlace() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		
		// 2) When
		boolean bol = pDao.isExistPlace(placeId);
		
		// 3) Then
		assertTrue("로우앤스윗은 DB에 존재해야함", bol);
	}
	
	// 장소 위치 확인 테스트
	@Test
	@Transactional
	public void testGetPlaceMapData() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		
		// 2) When
		Map<String, Object> map1 = pDao.getPlaceMapData(placeId);
		
		// 3) Then
		assertNotNull("장소 정보는 null이 아님", map1);
		System.out.println(map1);
	}
}