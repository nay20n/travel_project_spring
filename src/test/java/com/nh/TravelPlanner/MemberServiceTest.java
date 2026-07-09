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

import com.nh.service.MemberService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class MemberServiceTest {
	@Autowired
	MemberService mSvc;
	
	// 프로필 이미지 조회
	@Test
	public void testGetProfileImage() {
		// 1) Given
		int memberId = 1;
		
		// 2) When
		String profileImage = mSvc.getProfileImage(memberId);
		
		// 3) Then
		System.out.println("프로필 사진 : " + profileImage);
	}
	
	// 이메일이 동일한지 확인
	@Test
	public void testIsExistEmail() {
		// 1) Given
		String email = "a";
		
		// 2) When
		boolean result = mSvc.isExistEmail(email);
		
		// 3) Then : 이메일이 동일하지 않으면 실패
		assertTrue(email+"이라는 이메일은 존재하지 않습니다.", result);
	}
	
	//로그인 확인: 이메일 비밀번호 조회
	@Test
	public void testCanLogin() {
		// 1) Given
		String email = "a";
		String pw = "a";
		
		// 2) When
		boolean result = mSvc.canLogin(email, pw);
		
		// 3) Then : false일때 로그인 실패
		assertTrue("로그인 실패", result);
	}
	
	// 비밀번호 재설정 인증코드 수정
	@Test
	@Transactional
	public void testUpdateKey() {
		// 1) Given
		String email = "b";
		// 랜덤키 
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		
		String key = sb.toString();
		
		// 2) When
		mSvc.updateKey(key,email);
		
		// 3) Then : 인증코드 란이 비워져 있으면 오류
		assertNotNull("인증코드가 null이면 안됨.", key);
	}
	
	// 인증코드, 만료일시 조회
	@Test
	public void testIsValidCode() {
		// 1) Given
		String inputKey = "81HkvS";
		
		// 2) When
		boolean result = mSvc.isValidCode(inputKey);
		
		// 3) Then : 인증번호가 다르거나 인증시간이 오버하면 실패
		assertTrue("인증번호가 다르거나 인증 시간이 오버햇습니다.",result);
	}
	
	//회원 삽입 
	@Test
	@Transactional
	public void testAddMember() {
		// 1) Given
		String email = "f";
		// 랜덤키 
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		String pw = sb.toString();
		
		// 2) When
		mSvc.addMember(email, pw);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 프로필 이미지 수정 
	@Test
	@Transactional
	public void testModifyProfileImg() {
		// 1) Given
		int memberId = 1;
		String img = "img.png";
		
		// 2) When
		mSvc.modifyProfileImg(memberId,img);
		
		// 3) Then : sql 오류가 업다면 성공
	}
	
	// 이메일 수정
	@Test
	@Transactional
	public void testModifyEmail() {
		// 1) Given
		int memberId = 102;
		String email = "gmail";
		
		// 2) When
		mSvc.modifyEmail(memberId, email);
		
		// 3) Then : sql 오류가 업다면 성공
	}
	
	// 비밀번호 수정
	@Test 
	@Transactional
	public void testModifyPw() {
		// 1) Given
		int memberId = 102;
		String pw = "1234"; // 바꿀 비밀번호
		
		// 2) When
		mSvc.modifyPw(memberId, pw);
		
		// 3) Then : sql 오류가 없다면 성공
	}
	
	// 내 게시글 조회 (마이페이지)
	@Test
	public void testGetMyBoard() {
		// 1) Given
		int memberId = 1;
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> list = mSvc.getMyBoard(memberId, page);
		//System.out.println(list.size());
		// 3) Then : list가 NULL이면 안됨.
		assertNotNull("list가 null이면 안됨.", list);
		
		for(int i=0; i<=list.size()-1; i++) {
			System.out.println(list.get(i).get("bno") + " / " + list.get(i).get("title") + " / " + list.get(i).get("nickName"));
		}
	}
	
	// 내가 찜한 일정들 조회 (마이페이지)
	@Test
	public void testGetLikedBoard() {
		// 1) Given
		int memberId = 1;
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> list = mSvc.getLikedBoard(memberId, page);
		
		// 3) then
		assertNotNull("list가 null이면 안됨.", list);
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i).get("bno") + " / " + list.get(i).get("title") + " / " + list.get(i).get("nickName"));
		}
	}
	

	// 내가 댓글단 일정들 조회 (마이페이지)
	@Test
	public void testGetCommentBoard() {
		// 1) Given
		int memberId = 2;
		int page = 1;
		
		// 2) When
		List<Map<String, Object>> list = mSvc.getCommentBoard(memberId, page);
		
		// 3) then
		assertNotNull("list가 null이면 안됨.", list);
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i).get("bno") + " / " + list.get(i).get("title") + " / " + list.get(i).get("nickName"));
		}
	}
	
	//마이페이지 첫 호출
	@Test
	public void testGetMyPage() {
		// 1) Given
		int memberId = 2;
		
		// 2) When
		Map<String,Object> map = mSvc.getMyPage(memberId);
		
		// 3) Then : map이 null이 아니면 됨.
		assertNotNull("map이 null이면 안됨.", map);
		System.out.println(map.get("getMemberProfile"));
		System.out.println("내가 쓴 일정 : " + map.get("getMyBoard"));
		System.out.println("내가찜한 일정  : " + map.get("getLikedBoard"));
		System.out.println("내가 댓글 쓴 일정 : " + map.get("getCommentBoard"));
	}
	
	// 닉네임 조회
	@Test
	public void testGetNickName() {
		// 1)Given
		int memberId = 1;
		
		// 2) When
		String nickName = mSvc.getNickName(memberId);
		
		// 3) Then
		assertNotNull("nickName은 null이면 안됨.", nickName);
		System.out.println("nickName : " + nickName);
	}
	// 이메일 조회
	@Test
	public void testGetEmial() {
		// 1)Given
		int memberId = 1;
		
		// 2) When
		String email = mSvc.getEmail(memberId);
		
		// 3) Then
		assertNotNull("email은 null이면 안됨.", email);
		System.out.println("email : " + email);
	}
}