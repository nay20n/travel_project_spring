package com.nh.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.nh.service.MemberService;

@PropertySource("classpath:secret.properties")
@Configuration
@Controller
public class MemberController {
	@Value("${kakao.client.id}")
    private String KakaoClientId;
	@Value("${kakao.client.secret}")
	private String KakaoClientSecret;
	
	@Autowired
	MemberService mSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	// 카카오 로그인 요청 보내기
	@RequestMapping("/kakaologin")
	public String kakaologin(HttpSession session, Model model) {
		String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + KakaoClientId 
				+ "&redirect_uri=" + "http://localhost:9090/TravelPlanner/kakaologin/oauth";
		return "redirect:" + url;
	}
	// 카카오 로그인 처리
	@GetMapping("/kakaologin/oauth")
	public String kakaologinOauth(@RequestParam(required = false) String code,
		      @RequestParam(required = false) String error,
		      @RequestParam(name = "error_description", required = false) String errorDescription,
		      @RequestParam(required = false) String state, 
		      HttpSession session, RedirectAttributes rttr) {
		
//		System.out.println("### kakao 인가 코드 요청");
//	    System.out.println("code: " + code);
//	    System.out.println("error: " + error);
//	    System.out.println("errorDescription: " + errorDescription);
//	    System.out.println("state: " + state);
		
		// 코드로 토큰 발급
	    String authCode = code;
	    
	    // 헤더
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    // body
	    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	    body.add("grant_type", "authorization_code");
	    body.add("client_id", KakaoClientId);
	    body.add("redirect_uri", "http://localhost:9090/TravelPlanner/kakaologin/oauth");
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
	    
	    if(mSvc.isExistEmail(email)) {
	    	session.setAttribute("loginId", mSvc.getId(email));
	    	rttr.addFlashAttribute("msg", "로그인 되었습니다.");
	    } else {
	    	session.setAttribute("loginId", mSvc.addMember(email));
	    	rttr.addFlashAttribute("msg", "회원가입 되었습니다.");
	    }
	    
	    return "redirect:/";
	}
	
	
	@RequestMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		session.setAttribute("loginId", loginId);
		
		Map<String,Object> getMyPage = mSvc.getMyPage(loginId);
		model.addAttribute("getMyPage", getMyPage);
		
		return "MyPage";
	}
	
	@RequestMapping("/mypage/edit")
	public String edit(HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		session.setAttribute("loginId", loginId);
		
		Map<String, String> editPage = mSvc.getEditPage(loginId);
		model.addAttribute("editPage", editPage);
		
		return "EditInfo";
	}
	
	@RequestMapping("/forget")
	public String foget(HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		
		String email = mSvc.getEmail(loginId);
		String nickName = mSvc.getNickName(loginId);
		
		model.addAttribute("nickName", nickName);
		model.addAttribute("email", email);
		
		return "ResetPw";
	}
	
	@RequestMapping("/setpw")
	public String setpw(HttpSession session, Model model, RedirectAttributes rttr ,@RequestParam("key") String key) {
		int loginId = (int)session.getAttribute("loginId");
        
		// 지금 현재 세션에 저장된 key여야지만 접근 가능
        String keySession = (String)session.getAttribute("key");
		if(!keySession.equals(key)) {
			rttr.addFlashAttribute("rttrMsg", "해당 사이트의 접근권한이 없습니다.");
			return "redirect:/mypage/edit";
		}
        
		// 키가 존재하는지 + 만료되지 않았는지 서비스에서 확인
        boolean isValid = mSvc.isValidCode(key);
        if (!isValid) {
        	rttr.addFlashAttribute("rttrMsg", "키가 만료되었습니다.");
        	return "redirect:/mypage/edit";
        }
        
        model.addAttribute("loginId", loginId);
        model.addAttribute("key", key);
        return "SetPw"; 
	}
	
}
