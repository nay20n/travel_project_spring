package com.nh.dao;

import java.util.List;
import java.util.Map;

public interface MemberDao {
	/**
	 * 프로필 이미지 조회 HA-1
	 * @param memberId : 조회할 회원 아이디
	 * @return 프로필 이미지 파일명 
	 */
	String getProfileImage(int memberId);
	
	/**
	 * 이메일 조회 HA-4
	 * @param email : 비교할 이메일
	 * @return 같은 이메일이 있느 사람이 있다면 true
	 */
	boolean isExistEmail(String email);
	
	/**
	 * 이메일, 비밀번호 조회
	 * @param email 
	 * @param pw
	 * @return 둘다 존재하면(true), 둘 중 하나라도 존재하지 않으면 (false)
	 */
	boolean canLogin(String email, String pw);
	
	/**
	 * 비밀번호 재설정 인증코드 수정
	 * @param email : 비밀번호 ㅈ재설정할 이메일 이름
	 * @param key : 재설정할 인증코드
	 */
	void updateKey(String key, String email);
	
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
	 * 프로필 이미지 수정 
	 * @param memberId :수정할 회원의 아이디
	 * @param profileImg : 수정될 이미지
	 */
	void modifyProfileImg(int memberId, String profileImg);
	
	/**
	 * 이메일 수정
	 * @param memberId : 수정할 회원의 아이디
	 * @param email : 수정될 이메일
	 */
	void modifyEmail(int memberId, String email);
	
	/**
	 * 비밀번호 수정
	 * @param memberId : 수정할 회원의 아이디
	 * @param pw : 새로운 비밀번호
	 */
	void modifyPw(int memberId, String pw);
	
	/**
	 * 내 게시글들 조회
	 * @param memberId : 로그인한 사람의 아이디
	 * @param start: 보이는 게시글 시작
	 * @param end : 보이는 게시글 마지막
	 * @return 내 일정들
	 */
	List<Map<String,Object>> getMyBoard(int memberId, int start, int end);
	
	/**
	 * 내가 찜한 일정들 조회
	 * @param memberId : 로그인 한 사람의 아이디
	 * @param start: 보이는 게시글 시작
	 * @param end : 보이는 게시글 마지막
	 * @return 내가 찜한 일정들
	 */
	List<Map<String,Object>> getLikedBoard(int memberId, int start, int end);
	
	/**
	 * 내가 댓글 단 일정 조회
	 * @param memberId : 로그인 한 사람의 아이디
	 * @param start: 보이는 게시글 시작
	 * @param end : 보이는 게시글 마지막
	 * @return 내가 댓글단 일정들
	 */
	List<Map<String,Object>> getCommentBoard(int memberId, int start, int end);

	/**
	 * 프로필 & 활동한 내용 카운트 조회
	 * @param memberId : 로그인 한 사람의 아이디
	 * @return 사용자의 정보 + 활동 내역 카운트
	 */
	Map<String,Object> getMemberProfile(int memberId);
}
