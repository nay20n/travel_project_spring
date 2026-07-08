package com.nh.service;

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
	public List<Map<String, Object>> getComment(int bno, int start, int end) {
		return cDao.getComment(bno, start, end);
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
