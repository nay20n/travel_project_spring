package com.nh.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDaoImpl implements MemberDao{
	
	@Autowired
	SqlSession sqlSession;

	@Override
	public String getProfileImage(int memberId) {
		return sqlSession.selectOne("memberMapper.getProfileImage", memberId);
	}

	@Override
	public boolean isExistEmail(String email) {
		int cnt = sqlSession.selectOne("memberMapper.isExistEmail",email);
		return cnt > 0;
	}

	@Override
	public boolean canLogin(String email, String pw) {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("pw", pw);
		int cnt = sqlSession.selectOne("memberMapper.canLogin", map);
		return cnt > 0;
	}

	@Override
	public void updateKey(String key, String email) {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("key", key);
		sqlSession.update("memberMapper.updateKey", map);
	}

	@Override
	public boolean isValidCode(String key) {
		int cnt = sqlSession.selectOne("memberMapper.isValidCode", key);
		return cnt == 1;
	}

	@Override
	public void addMember(String email, String pw) {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("pw", pw);
		sqlSession.insert("memberMapper.addMember",map);
	}

	@Override
	public void modifyProfileImg(int memberId, String profileImg) {
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("profileImg", profileImg);
		sqlSession.update("memberMapper.modifyProfileImg", map);
	}

	@Override
	public void modifyEmail(int memberId, String email) {
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("email", email);
		sqlSession.update("memberMapper.modifyEmail", map);
	}

	@Override
	public void modifyPw(int memberId, String pw) {
		Map<String,Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("pw", pw);
		sqlSession.update("memberMapper.modifyPw", map);
	}

	@Override
	public List<Map<String, Object>> getMyBoard(int memberId, int start, int end) {
		
		//한번에 보여줄 bno들 계산
		
		Map<String, Integer> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("start",start);
		map.put("end",end);
		
		return sqlSession.selectList("memberMapper.getMyBoard", map);
	}

	@Override
	public List<Map<String, Object>> getLikedBoard(int memberId, int start, int end) {
		Map<String, Integer> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("start",start);
		map.put("end",end);
		
		return sqlSession.selectList("memberMapper.getLikedBoard",map);
	}

	@Override
	public List<Map<String, Object>> getCommentBoard(int memberId, int start, int end) {
		Map<String, Integer> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("start",start);
		map.put("end",end);
		
		return sqlSession.selectList("memberMapper.getLikedBoard", map);
	}

	@Override
	public Map<String, Object> getMemberProfile(int memberId) {
		return sqlSession.selectOne("memberMapper.getMemberProfile", memberId);
	}
	
}
