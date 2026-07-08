package com.nh.dao;

import java.util.List;
import java.util.Map;

public interface CommentDao {
	
	/**
	 * 댓글 개수 카운트
	 * @param bno : 현재 보고 있는 게시글 번호 
	 * @return 현재 보고 있는 게시글의 댓글 수 
	 */
	int CountComment(int bno);
	
	/**
	 * 댓글 목록 조회
	 * @param bno : 현재 보고 있는 게시굴의 번호
	 * @param page : 페이지 번호
	 * @return 댓글 목록 리스트
	 */
	List<Map<String, Object>> getComment(int bno, int start, int end);
	
	/**
	 * 게시글 댓글 삭제
	 * @param cno : 지울 댓글 번호
	 * @param id : 지울려는 당사자의 아이디
	 */
	void deleteComment(int cno, int id);
	
	/**
	 * 게시글 댓글 추가
	 * @param bno : 댓글 추가될 게시글 번호
	 * @param writerId : 댓글 작성자
	 * @param content : 댓글 내용 
	 */
	void insertComment(int bno, int writerId, String content);
	
	/**
	 * 게시글 댓글 수정
	 * @param cno : 수정할 댓글 번호
	 * @param content : 수정할 댓글 내용
	 */
	void modifyComment(int cno, String content);
	
	/**
	 * 게시글 댓글 삭제(게시글 삭제를 위해 전체 삭제)
	 * @param bno
	 */
	void deleteCommentByBno(int bno);
}
