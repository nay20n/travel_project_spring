package com.nh.service;

import java.util.List;
import java.util.Map;

public interface BlockService {
	/**
	 * 블럭 삽입
	 * @param bno
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int addBlock(int bno, String startTime, String endTime);
	
	/**
	 * 블럭 장소 수정
	 * @param blockIdx
	 * @param placeId
	 */
	void modifyBlockPlace(int blockIdx, String placeId);
	
	/**
	 * 블럭 삭제(해당 인덱스만)
	 * @param blockIdx
	 */
	void deleteBlock(int blockIdx);
	
	/**
	 * 블럭 색 수정
	 * @param blockIdx
	 * @param colorIdx
	 */
	void modifyBlockColor(int blockIdx, int colorIdx);
	
	/**
	 * 블럭 시간 수정
	 * @param blockIdx
	 * @param startTime
	 * @param endTime
	 */
	void modifyBlockTime(int blockIdx, String startTime, String endTime);
	
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
	List<Map<String,Object>> getColors();
}
