package com.nh.dao;

import java.util.List;
import java.util.Map;

public interface BlockDao {
	/**
	 * 블럭 삽입
	 * @param bno
	 * @param startTime
	 * @param endTime
	 * @return 블럭 인덱스
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
	 * 블럭 삭제(해당 게시글 전체)
	 * @param bno
	 * @param memberId
	 */
	void deleteBlock(int bno, int memberId);
	
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
	 * 하루 치 일정 삭제
	 * @param bno
	 * @param date(지우려는 날짜"YYYYMMDD")
	 */
	void deleteBlockInDate(int bno, String date);
	
	/**
	 * 블럭 색 조회(색 변경 팝업)
	 * @return
	 */
	List<Map<String, Object>> getColors();
	
	/**
	 * 게시글 전체 블럭 복사
	 * @param newBno 새 게시글 번호
	 * @param bno 복제하고 싶은 게시글 번호
	 * @param newStartTime 새 출발일
	 */
	void copyBlock(int newBno, int bno, String newStartTime);
	
	/**
	 * 장소 들어있는 블럭 조회(ai 견적용)
	 * @param bno
	 * @return name, address
	 */
	List<Map<String, Object>> getBlocksForAiCount(int bno);
	
	/**
	 * 장소 들어있는 블럭 조회(구글 Routes 용)
	 * @param bno
	 * @param inputTime 해당날짜 반드시 'YYYY-MM-DD'형식
	 * @return placeId
	 */
	List<String> getBlocksForRoutes(int bno, String inputTime);
}
