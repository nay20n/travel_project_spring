package com.nh.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDaoImpl implements CommentDao{

	@Autowired
	SqlSession sqlSession;
	
	@Override
	public int CountComment(int bno) {
		return sqlSession.selectOne("CommentMapper.CountComment", bno);
	}

	@Override
	public List<Map<String, Object>> getComment(int bno, int start, int end) {
		Map<String, Integer> map = new HashMap<>();
		map.put("bno", bno);
		map.put("start", start);
		map.put("end", end);
		return sqlSession.selectList("CommentMapper.getComment", map);
	}

	@Override
	public void deleteComment(int cno, int id) {
		Map<String, Integer> map = new HashMap<>();
		map.put("cno", cno);
		map.put("id", id);
		sqlSession.delete("CommentMapper.deleteComment", map);
	}

	@Override
	public void insertComment(int bno, int writerId, String content) {
		Map<String, Object> map = new HashMap<>();
		map.put("bno", bno);
		map.put("writerId", writerId);
		map.put("content", content);
		sqlSession.insert("CommentMapper.insertComment", map);
	}

	@Override
	public void modifyComment(int cno, String content) {
		Map<String, Object> map = new HashMap<>();
		map.put("cno", cno);
		map.put("content", content);
		sqlSession.update("CommentMapper.modifyComment", map);
		
	}
}
