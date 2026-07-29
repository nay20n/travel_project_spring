package com.nh.service;

import java.util.List;
import java.util.Map;

public interface MemberService {
	/**
	 * 프로필 이미지 조회
	 * @param memberId : 조회할 회원 아이디
	 * @return 프로필 이미지 파일명 
	 */
	String getProfileImage(int memberId);
	
	/**
	 * 로그인한 사람의 닉네임 조회
	 * @param memberId : 조회할 사람의 아이디
	 * @return 조회된 닉네임
	 */
	String getNickName(int memberId); 
	
	/**
	 * 로그인한 사람의 이메일 조회
	 * @param memberId : 조회할 사람의 아이디 
	 * @return 조회된 닉네임
	 */
	String getEmail(int memberId);
	
	/**
	 * 정보 수정 페이지에 필요한 정보(프로필 사진 조회, 닉네임, 이메일) 한번에 조회
	 * @param memberId : 조회할 사람의 아이디 
	 * @return 조회된 닉네임, 프로필 사진, 이메일
	 */
	Map<String, String> getEditPage(int memberId);
	
	/**
	 * 이메일 조회 HA-4
	 * @param email 비교할 이메일
	 * @return 이메일이 존재(true) 존재안하면(false)
	 */
	boolean isExistEmail(String email);
	
	/**
	 * 이메일, 비밀번호 조회
	 * @param email 
	 * @param pw
	 * @return 둘다 존재하면(true), 둘 중 하나라도 존재하지 않으면 (false)
	 */
	Integer canLogin(String email, String pw);
	
	/**
	 * 비밀번호 재설정 인증코드 수정
	 * @param email : 비밀번호 재설정할 이메일 이름
	 * @return 
	 */
	String updateKey(String email);
	
	/**
	 * 인증코드, 만료일시 조회
	 * @param key : 비교할 키
	 * @return 동일하면 True, 실패 false
	 */
	boolean isValidCode(String key);
	
	/**
	 * 회원 삽입
	 * @param email : 삽입할 이메일
	 * @param pw : 삽입할 계정 비밀번호
	 */
	void addMember(String email, String pw);
	
	/**
	 * 회원 삽입(소셜 가입)
	 * @param email : 삽입할 이메일
	 */
	int addMember(String email);
	
	/**
	 * 프로필 이미지 수정 
	 * @param memberId :수정할 회원의 아이디
	 * @param profileImg : 수정될 이미지
	 */
	void modifyProfileImg(int memberId, String profileImg);
	
	/**
	 * 비밀번호 수정
	 * @param memberId : 수정할 회원의 아이디
	 * @param pw : 새로운 비밀번호
	 */
	void modifyPw(int memberId, String pw);
	
	/**
	 * 이메일 수정
	 * @param memberId : 수정할 회원의 아이디
	 * @param email : 수정될 이메일
	 * @param nickName : 새로운 닉네임
	 */
	void modifyInfo(int memberId, String email, String nickName);
	
	/**
	 * 내 게시글들 조회 (마이페이지)
	 * @param memberId : 로그인한 사람의 아이디
	 * @param page : 현재 페이지 번호
	 * @return 내 일정들
	 */
	List<Map<String,Object>> getMyBoard(int memberId, int page);
	
	/**
	 * 내가 찜한 일정들 조회 (마이페이지)
	 * @param memberId : 로그인한 사람의 아이디
	 * @param page : 현재 페이지 번호
	 * @return 내가 찜한 일정들
	 */
	List<Map<String,Object>> getLikedBoard(int memberId, int page);
	
	/**
	 * 내가 댓글 단 일정 조회 (마이페이지)
	 * @param memberId : 로그인 한 사람의 아이디 
	 * @param page : 현재 페이지 번호
	 * @return 내가 댓글 단 일정들 
	 */
	List<Map<String,Object>> getCommentBoard(int memberId, int page);
	
	
	/**
	 * 마이페이지 한번에 처음 호출 (마이페이지)
	 * @param memberId : 로그인 한 사람의 아이디
	 * @return
	 */
	Map<String,Object> getMyPage(int memberId);
	
	
	/**
	 * 로그인한 사람의 비밀번호 조회
	 * @param memberId : 조회할 사람의 아이디 
	 * @return 조회된 비밀번호
	 */
	String getPw (int memberId);
	
	/**
	 * 아이디 조회
	 * @param email
	 * @return memberId
	 */
	int getId (String email);
	
	/**
	 * 카카오로 email 조회
	 * @param authCode
	 * @return email
	 */
	String getEmailByKakao(String code);
}
