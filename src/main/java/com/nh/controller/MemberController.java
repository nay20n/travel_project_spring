package com.nh.controller;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nh.service.ExternalApiService;
import com.nh.service.MemberService;

@PropertySource("classpath:secret.properties")
@Configuration
@Controller
public class MemberController {
	@Value("${kakao.client.id}")
    private String KakaoClientId;
	@Value("${kakao.client.secret}")
	private String KakaoClientSecret;
	@Value("${naver.client.id}")
    private String NaverClientId;
	@Value("${naver.client.secret}")
	private String NaverClientSecret;
	
	@Autowired
	MemberService mSvc;
	@Autowired
	ExternalApiService eSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	// 카카오 로그인 요청 보내기
	@GetMapping("/kakaologin")
	public String kakaologin(@RequestParam String mapping) {
		String redirect = "http://52.199.216.149:9090/TravelPlanner/kakaologin/editInfo";
		if("login".equals(mapping)) {
			redirect = "http://52.199.216.149:9090/TravelPlanner/kakaologin/mainHome";
		}
		String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + KakaoClientId 
				+ "&redirect_uri=" + redirect;
		return "redirect:" + url;
	}
	// 카카오 로그인 처리
	@GetMapping("/kakaologin/mainHome")
	public String kakaologinMainHome(@RequestParam(required = false) String code,
		      @RequestParam(required = false) String error,
		      @RequestParam(name = "error_description", required = false) String errorDescription,
		      @RequestParam(required = false) String state, 
		      HttpSession session, RedirectAttributes rttr) {
		
//		System.out.println("### kakao 인가 코드 요청");
//	    System.out.println("code: " + code);
//	    System.out.println("error: " + error);
//	    System.out.println("errorDescription: " + errorDescription);
//	    System.out.println("state: " + state);
		
	    String email = mSvc.getEmailByKakao(code,"mainHome");
	    
	    if(mSvc.isExistEmail(email)) {
	    	session.setAttribute("loginId", mSvc.getId(email));
	    	rttr.addFlashAttribute("msg", "로그인 되었습니다.");
	    } else {
	    	session.setAttribute("loginId", mSvc.addMember(email));
	    	rttr.addFlashAttribute("msg", "회원가입 되었습니다.");
	    }
	    
	    return "redirect:/";
	}
	// 카카오 로그인 처리(정보수정)
	@GetMapping("/kakaologin/editInfo")
	public String kakaologinEditInfo(@RequestParam(required = false) String code,
			@RequestParam(required = false) String error,
			@RequestParam(name = "error_description", required = false) String errorDescription,
			@RequestParam(required = false) String state, 
			HttpSession session, RedirectAttributes rttr) {
		
		String email = mSvc.getEmailByKakao(code,"editInfo");
		
		if(mSvc.isExistEmail(email)) {
			session.setAttribute("loginId", mSvc.getId(email));
			rttr.addFlashAttribute("msg", "로그인 되었습니다.");
		} else {
			rttr.addFlashAttribute("msg", "로그인한 이메일과 다릅니다. 정보를 다시 확인해주세요.");
			return "redirect:/";
		}
		
		return "redirect:http://52.199.216.149:9090/TravelPlanner/mypage/edit";
	}
	
	// 신규 회원 인증번호 맞는지 체크 
	@GetMapping("/joinCode")
	public String checkAuthCode (RedirectAttributes rttr, HttpSession session, String inputKey) {
		// 지금 현재 세션에 저장된 key여야지만 접근 가능
		String keySession = (String)session.getAttribute("key");
		//System.out.println("발급된 키 "+keySession);
		//System.out.println("입력된 키 "+inputKey);
		if(!keySession.equals(inputKey)) {
			rttr.addFlashAttribute("msg", "인증번호가 일치하지 않습니다. 다시 시도해주세요.");
		} else {
			int memberId = mSvc.addMember((String)session.getAttribute("email"));
			//System.out.println(memberId);
			session.setAttribute("loginId", memberId);
			rttr.addFlashAttribute("msg", "회원 가입이 완료 되었습니다. 꼭 비밀번호를 변경해주세요.");
			return "SetPw";
		}

		return "redirect:/";
	}
	// 네이버 로그인 요청 보내기
	@GetMapping("/naverlogin")
	public String naverlogin(@RequestParam String mapping) {
		String redirect = "http://52.199.216.149:9090/TravelPlanner/naverlogin/editInfo";
		if("login".equals(mapping)) {
			redirect = "http://52.199.216.149:9090/TravelPlanner/naverlogin/mainHome";
		}
		
		SecureRandom random = new SecureRandom();
	    String state = new BigInteger(130, random).toString(32);

	    String url = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
	            + "&client_id=" + NaverClientId
	            + "&redirect_uri=" + redirect
	            + "&state=" + state;
		
	    //System.out.println ("url : "+url);
		return "redirect:" + url;
	}
	// 네이버 로그인 처리
	@GetMapping("/naverlogin/mainHome")
	public String naverlogin(@RequestParam(required = false) String code,
			@RequestParam(required = false) String state, 
			HttpSession session, RedirectAttributes rttr) {
		
	    String email = mSvc.getEmailByNaver(code, state);
	    
	    //System.out.println("email : " + email);
	    
	    if(mSvc.isExistEmail(email)) {
	    	session.setAttribute("loginId", mSvc.getId(email));
	    	rttr.addFlashAttribute("msg", "로그인 되었습니다.");
	    } else {
	    	session.setAttribute("loginId", mSvc.addMember(email));
	    	//System.out.println("loginId : " +  mSvc.addMember(email));
	    	rttr.addFlashAttribute("msg", "회원가입 되었습니다.");
	    }
	    
	    return "redirect:/";
	}
	
	// 카카오 로그인 처리(정보수정)
	@GetMapping("/naverlogin/editInfo")
	public String naverloginEditInfo(@RequestParam(required = false) String code,
			@RequestParam(required = false) String state, 
			HttpSession session, RedirectAttributes rttr) {
		
		String email = mSvc.getEmailByNaver(code,state);
		
		if(mSvc.isExistEmail(email)) {
			session.setAttribute("loginId", mSvc.getId(email));
			rttr.addFlashAttribute("msg", "로그인 되었습니다.");
		} else {
			rttr.addFlashAttribute("msg", "로그인한 이메일과 다릅니다. 정보를 다시 확인해주세요.");
			return "redirect:/";
		}
		
		return "redirect:http://52.199.216.149:9090/TravelPlanner/mypage/edit";
	}
	
	
	@RequestMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
		int loginId = (int)session.getAttribute("loginId");
		session.setAttribute("loginId", loginId);
		
		String email = mSvc.getEmail(loginId);
		
		Map<String,Object> getMyPage = mSvc.getMyPage(loginId);
		model.addAttribute("getMyPage", getMyPage);
		model.addAttribute("email", email);
		
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
	public String foget(HttpSession session, String email, Model model) {
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
