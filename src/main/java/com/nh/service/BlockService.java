package com.nh.service;

import java.util.List;
import java.util.Map;

public interface BlockService {
	/**
	 * 블럭 삽입
	 * @param bno
	 * @param startTime
	 * @param endTime
	 * @return key:blockIdx->삽입된 블럭의 인덱스 
	 *         key:blocks->삽입 후의 블럭들 
	 */
	Map<String,Object> addBlock(int bno, String startTime, String endTime);
	
	/**
	 * 블럭 장소 수정
	 * @param blockIdx
	 * @param placeId
	 * @param bno
	 * @return 수정된 모든 블럭들 
	 */
	List<Map<String, Object>> modifyBlockPlace(int blockIdx, String placeId, int bno);
	
	/**
	 * 블럭 삭제(해당 인덱스만)
	 * @param blockIdx
	 * @return 삭제된 후의 모든 블럭들 
	 */
	List<Map<String,Object>> deleteBlock(int blockIdx, int bno);
	
	/**
	 * 블럭 색 수정 후 블럭들 리턴 
	 * @param blockIdx
	 * @param colorIdx
	 * @return 블럭 색 수정 후 블럭들 리턴
	 */
	List<Map<String,Object>> modifyBlockColor(int blockIdx, int colorIdx, int bno);
	
	/**
	 * 블럭 시간 수정
	 * @param blockIdx
	 * @param startTime
	 * @param endTime
	 * @return 수정 후의 모든 블럭들
	 */
	 List<Map<String,Object>> modifyBlockTime(int blockIdx, String startTime, String endTime, int bno);
	
	/**
	 * 블럭 AI 반영여부 수정
	 * @param blockIdx
	 * @param isCheckedAi DB(1:체크됨/0:아님)
	 */
	void modifyBlockCheckedAi(int blockIdx, boolean isCheckedAi);
	
	/**
	 * 블럭 이동 시간 수정
	 * @param blockIdx
	 * @param travelTime(분)
	 */
	void modifyBlockTravelTime(int blockIdx, int travelTime);
	
	/**
	 * 블럭 정보 조회(블럭 팝업)
	 * @param blockIdx
	 * @return 블럭정보(startTime, endTime, checkedAi, colorIdx, colorCode, name, category, address, lat, lng)
	 */
	Map<String,Object> getBlockDetail(int blockIdx);
	
	/**
	 * 게시글 블럭 전체 조회
	 * @param bno
	 * @return (blockIdx, startTime, endTime, checkedAi, travelTime, colorIdx, colorCode, name, lat, lng)
	 */
	List<Map<String, Object>> getAllBlocks(int bno);
	
	/**
	 * 게시글의 블록 정보 특정 시간 사이 조회(시작시간 기준)
	 * @param bno
	 * @param inputStartTime
	 * @param inputEndTime
	 * @return (blockIdx, startTime, endTime, checkedAi, travelTime, colorIdx, colorCode, name, lat, lng)
	 */
	List<Map<String, Object>> getBlocksBetween(int bno, String inputStartTime, String inputEndTime);
	
	/**
	 * 블럭 색 조회(색 변경 팝업)
	 * @return
	 */
	List<Map<String, Object>> getColors();
	
	/**
	 * 장소 들어있는 블럭 조회(구글 Routes 용)
	 * @param bno
	 * @param inputTime 해당날짜 반드시 'YYYY-MM-DD'형식
	 * @return placeId
	 */
	List<String> getBlocksForRoutes(int bno, String inputTime);
}
