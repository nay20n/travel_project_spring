package com.nh.service;

import java.util.List;
import java.util.Map;

public interface AiBlockService {
	
	/**
	 * AI추천 블록 조회
	 * @param bno : 조회할 게시글 번호
	 * @return : AI 블럭들 조회
	 */
	List<Map<String,Object>> getAiBlock(int bno);
	
	/**
	 * AI 블럭 삭제
	 * @param bno : 게시글의 인덱스 
	 */
	void deleteAiBlock(int bno);
	
	/**
	 * AI 블럭 삽입 (삭제 후 삽입)
	 * @param bno : 삽입될 게시글 번호
	 * @param placeId : 삽입할 장소
	 * @param startTime : 블럭 시작
	 * @param endTime : 블럭 끝 
	 * @param travelTime : 중간 여행 기간
	 */
	void insertAiBlock(int bno, String placeId, String startTime, String endTime, int travelTime);

	/**
	 * AI 블록 내 일정에 복제
	 * @param bno : 복제할 게시글의 번호
	 */
	void copyAiBlock(int bno);
}
