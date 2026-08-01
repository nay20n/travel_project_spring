package com.nh.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface ExternalApiService {
	/**
	 * 경로 문자열 배열 얻기
	 * @param travelMode 이동수단
	 * @param placeIds 장소아이디들
	 * @return api 응답 성공 시: routes.polyline.encodedPolyline / 실패 시: "fail"
	 */
	String getRoute(String travelMode, List<String> placeIds);
	
	/**
	 * ai 견적 확인
	 * @param bno
	 * @return transportCost, foodCost, roomCost, etcCost, maxCost
	 */
	Map<String,Object> searchCost(int bno);
	
	/**
	 * ai 일정 추천 및 db에 없는 장소 삽입
	 * @param userBlocks 하루 일정
	 * @param bno
	 * @param arrPlaceCity
	 * @return 추천 하루 일정
	 */
	List<Map<String,Object>> searchAiRecommend(List<Map<String, Object>> userBlocks, int bno, String arrPlaceCity);
	
	/**
	 * 이메일 보내기
	 * @param session : key 값 보내기
	 * @param email : 보낼 이메일
	 * @param nickName : 보낼 사람의 닉네임
	 * @param pageType : 이메일을 어디서 보내는지(페이지 이름)
	 */
	String sendEmail(String email, String nickName, String pageType);
	
	/**
	 * 비밀번호 재설정 인증코드 수정
	 * @param email : 비밀번호 재설정할 이메일 이름
	 * @return 
	 */
	String updateKey(String email, String pageType);
	
	/**
	 * 이미지를 보고 어느 랜드마크인지 리턴
	 * @param imgSrc
	 * @return : 예상된 추출 장소 이름
	 */
	public Map<String, Object> getVisionResult(MultipartFile fil);

}
