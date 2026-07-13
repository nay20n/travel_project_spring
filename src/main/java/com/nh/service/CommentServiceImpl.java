package com.nh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.dao.CommentDao;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	CommentDao cDao;
	
	@Override
	public int CountComment(int bno) {
		return cDao.CountComment(bno);
	}

	@Override
	public Map<String, Object> getComment(int bno, int page) {
		Map<String, Object> map1 = new HashMap<>();
		
		// 페이지네이션
		int end = page * 5;
		int start = end - 4;
		List<Map<String, Object>> comments = cDao.getComment(bno, start, end);
		int lastPageNum = (cDao.CountComment(bno)-1)/5+1;
		
		map1.put("comments", comments);
		map1.put("lastPageNum", lastPageNum);
		return map1;
	}

	@Override
	public void deleteComment(int cno, int id) {
		cDao.deleteComment(cno, id);
	}

	@Override
	public void insertComment(int bno, int writerId, String content) {
		cDao.insertComment(bno, writerId, content);
	}

	@Override
	public void modifyComment(int cno, String content) {
		cDao.modifyComment(cno, content);
	}

	
}
