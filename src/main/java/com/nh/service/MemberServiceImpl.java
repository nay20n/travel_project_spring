package com.nh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.nh.dao.MemberDao;

@PropertySource("classpath:secret.properties")
@Configuration
@Service
public class MemberServiceImpl implements MemberService {
	@Value("${kakao.client.id}")
    private String KakaoClientId;
	@Value("${kakao.client.secret}")
	private String KakaoClientSecret;
	@Value("${naver.client.id}")
    private String NaverClientId;
	@Value("${naver.client.secret}")
	private String NaverClientSecret;
	
	@Autowired
	MemberDao mDao;
	
	@Override
	public String getProfileImage(int memberId) {
		return mDao.getProfileImage(memberId);
	}
	
	@Override
	public String getNickName(int memberId) {
		return mDao.getNickName(memberId);
	}

	@Override
	public String getEmail(int memberId) {
		return mDao.getEmail(memberId);
	}

	@Override
	public Map<String, String> getEditPage(int memberId) {
		String profileImg = mDao.getProfileImage(memberId);
		String nickName = mDao.getNickName(memberId);
		String email = mDao.getEmail(memberId);
		
		Map<String,String> ret = new HashMap<>();
		ret.put("profile", profileImg);
		ret.put("nickName", nickName);
		ret.put("email", email);
		
		return ret;
	}
	
	@Override
	public boolean isExistEmail(String email) {
		return mDao.isExistEmail(email);
	}

	@Override
	public Integer canLogin(String email, String pw) {
		return mDao.canLogin(email,pw);
	}

	@Override
	public String updateKey(String email) {
		
		// 랜덤키
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		mDao.updateKey(sb.toString(), email);
		return (String)sb.toString(); 
	}
	
	@Override
	public boolean isValidCode(String key) {
		return mDao.isValidCode(key);
	}

	@Override
	public void addMember(String email, String pw) {
		mDao.addMember(email, pw);
	}

	@Override
	public void modifyProfileImg(int memberId, String profileImg) {
		mDao.modifyProfileImg(memberId, profileImg);
	}


	@Override
	public void modifyPw(int memberId, String pw) {
		mDao.modifyPw(memberId, pw);
	}
	
	@Override
	public void modifyInfo(int memberId, String email, String nickName) {
		mDao.modifyEmail(memberId, email);
		mDao.modifyNickName(memberId, nickName);
	}
	
	@Override
	public List<Map<String, Object>> getMyBoard(int memberId, int page) {
		int end = 10 * page;
		int start = end - 9;
		return mDao.getMyBoard(memberId, start, end);
	}

	@Override
	public List<Map<String, Object>> getLikedBoard(int memberId, int page) {
		int end = 10 * page;
		int start = end - 9;
		return mDao.getLikedBoard(memberId, start, end);
	}

	@Override
	public List<Map<String, Object>> getCommentBoard(int memberId, int page) {
		int end = 10 * page;
		int start = end - 9;
		return mDao.getCommentBoard(memberId, start, end);
	}

	@Override
	public Map<String,Object> getMyPage(int memberId) {
		
		Map<String,Object> map = new HashMap<>();
		map.put("getMemberProfile", mDao.getMemberProfile(memberId));
		map.put("getMyBoard", mDao.getMyBoard(memberId, 1, 10));
		map.put("getLikedBoard", mDao.getLikedBoard(memberId, 1, 10));
		map.put("getCommentBoard", mDao.getCommentBoard(memberId, 1, 10));
		
		return map;
	}

	@Override
	public String getPw(int memberId) {
		return mDao.getpw(memberId);
	}

	@Override
	public int getId(String email) {
		return mDao.getId(email);
	}

	@Override
	public int addMember(String email) {
		// 비밀번호 생성
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		String pw = sb.toString();
		return mDao.addMember(email, pw);
	}

	@Override
	public String getEmailByKakao(String code, String mapping) {
		// 코드로 토큰 발급
	    String authCode = code;
	    
	    // 헤더
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    // body
	    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	    body.add("grant_type", "authorization_code");
	    body.add("client_id", KakaoClientId);
	    body.add("redirect_uri", "http://localhost:9090/TravelPlanner/kakaologin/"+mapping);
	    body.add("code", authCode);
	    body.add("client_secret", KakaoClientSecret);
	    
	    // Http요청 객체
	    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
	    // Kakao API 호출
	    ResponseEntity<JsonNode> response =
	        new RestTemplate().exchange(
	            "https://kauth.kakao.com/oauth/token",
	            HttpMethod.POST,
	            httpEntity,
	            JsonNode.class);
	    
	    JsonNode jsonNode = response.getBody();
	    
	    // 토큰으로 이메일 조회
	    String token = jsonNode.get("access_token").asText();
	    
	    // 헤더
	    headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    headers.add("Authorization", "bearer " + token);
	    
	    // Http요청 객체
	    httpEntity = new HttpEntity<>(headers);
	    
	    // Kakao API 호출
	    response = new RestTemplate().exchange(
	            "https://kapi.kakao.com/v2/user/me",
	            HttpMethod.GET,
	            new HttpEntity<>(headers),
	            JsonNode.class);
	    
	    //System.out.println(response.getBody());
	    jsonNode = response.getBody();
	    String email = jsonNode.get("kakao_account").get("email").asText();
		return email;
	}
	
	@Override
	public String getEmailByNaver(String code, String state) {
		// 코드로 토큰 발급
	    
	    // 헤더
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    // body
	    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	    body.add("grant_type", "authorization_code");
	    body.add("client_id", NaverClientId);
	    body.add("client_secret", NaverClientSecret);
	    body.add("code", code);
	    body.add("state", state);
	    
	    
	    body.add("redirect_uri", "http://localhost:9090/TravelPlanner/naverlogin/mainHome");
	    
	    
	    // Http요청 객체
	    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
	    // Naver API 호출
	    ResponseEntity<JsonNode> response =
	        new RestTemplate().exchange(
	            "https://nid.naver.com/oauth2.0/token",
	            HttpMethod.POST,
	            httpEntity,
	            JsonNode.class);
	    
	    JsonNode jsonNode = response.getBody();
	    
	    // 토큰으로 이메일 조회
	    String token = jsonNode.get("access_token").asText();
	    
	    // 헤더
	    headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    headers.add("Authorization", "Bearer " + token);
	    
	    // Http요청 객체
	    httpEntity = new HttpEntity<>(headers);
	    
	    // Kakao API 호출
	    response = new RestTemplate().exchange(
	            "https://openapi.naver.com/v1/nid/me",
	            HttpMethod.GET,
	            new HttpEntity<>(headers),
	            JsonNode.class);
	    
	    //System.out.println(response.getBody());
	    jsonNode = response.getBody();
	    String email = jsonNode.get("response").get("email").asText();
		return email;
	}

	
}
