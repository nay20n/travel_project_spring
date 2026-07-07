package com.nh.TravelPlanner;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}