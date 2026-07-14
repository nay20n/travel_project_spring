package com.nh.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.dao.PlaceDao;

@Service
public class PlaceServiceImpl implements PlaceService {
	@Autowired
	PlaceDao pDao;
	
	// 장소 정보 조회
	@Override
	public Map<String, Object> getPlaceDetail(String placeId, int memberId) {
		Map<String, Object> map1 = pDao.viewPlaceDetails(placeId, memberId);
		map1.put("images", pDao.getPlaceImages(placeId));
		return map1;
	}
	
	// 찜한 장소 추가
	@Override
	public void addLikedPlace(int memberId, String placeId) {
		pDao.addLikedPlace(memberId, placeId);
	}
	
	// 찜한 장소 삭제
	@Override
	public void deleteLikedPlace(int memberId, String placeId) {
		pDao.deleteLikedPlace(memberId, placeId);
	}
	// 장소 댓글 가져오기
	@Override
	public List<Map<String, Object>> getReviews(String placeId, int page) {
		// 페이지네이션(10개씩 조회)
		int start = page*5-4;
		int end = page*5;
		return pDao.getReviews(placeId, start, end);
	}
	// 댓글 추가 
	@Override
	public void addReview(int memberId, String placeId, String content, int rating, String image) {
		pDao.addReview(memberId, placeId, content, rating, image);
	}

	@Override
	public void modifyReview(int reviewIdx, int memberId, String content, int rating, String image) {
		pDao.modifyReview(reviewIdx, memberId, content, rating, image);
	}
	// 댓글 삭제 
	@Override
	public void deleteReview(int reviewIdx, int memberId) {
		pDao.deleteReview(reviewIdx, memberId);
	}

	@Override
	public void addPlace(String placeId, String name, String category, String address, double lat, double lng,
			String websiteUrl, String businessHours) {
		if(pDao.isExistPlace(placeId)) return;
		pDao.addPlace(placeId, name, category, address, lat, lng, websiteUrl, businessHours);
	}
	
	
}
