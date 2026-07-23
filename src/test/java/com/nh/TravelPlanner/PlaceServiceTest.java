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
import com.nh.service.PlaceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class PlaceServiceTest {
	@Autowired
	PlaceService pSvc;
	
	// 장소 정보 조회 테스트
	@Test
	public void testGetPlaceDetails() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		int memberId = 1;
		
		// 2) When
		Map<String, Object> listDetail = pSvc.getPlaceDetail(placeId, memberId);
		
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
		pSvc.addLikedPlace(memberId, placeId);
		
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
		pSvc.deleteLikedPlace(memberId, placeId);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 장소 댓글 목록 조회 테스트
	@Test
	public void testViewReviews() {
		// 1) Given
		String placeId = "ChIJofoWUQCNaDURDqIZjAjYMU8";
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> listReview = pSvc.getReviews(placeId, page);
		
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
		pSvc.addReview(memberId, placeId, content, rating, image);
		
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
		pSvc.modifyReview(reviewIdx, memberId, content, rating, image);
		
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
		pSvc.deleteReview(reviewIdx, memberId);
		
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
		String imgs = "테스트이미지 하나 둘 셋";
		// 2) When
		pSvc.addPlace(placeId, name, category, address, lat, lng, websiteUrl, businessHours, imgs);
		
		// 3) Then : sql 오류가 없다면 성공
	}
}