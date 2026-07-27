package com.nh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.dao.MemberDao;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDao mDao;
	
	@Override
	public String getProfileImage(int memberId) {
		return mDao.getProfileImage(memberId);
	}
	
	@Override
	public String getNickName(int memberId) {
		return mDao.getNickName(memberId);
	}

	@Override
	public String getEmail(int memberId) {
		return mDao.getEmail(memberId);
	}

	@Override
	public Map<String, String> getEditPage(int memberId) {
		String profileImg = mDao.getProfileImage(memberId);
		String nickName = mDao.getNickName(memberId);
		String email = mDao.getEmail(memberId);
		
		Map<String,String> ret = new HashMap<>();
		ret.put("profile", profileImg);
		ret.put("nickName", nickName);
		ret.put("email", email);
		
		return ret;
	}
	
	@Override
	public boolean isExistEmail(String email) {
		return mDao.isExistEmail(email);
	}

	@Override
	public Integer canLogin(String email, String pw) {
		return mDao.canLogin(email,pw);
	}

	@Override
	public String updateKey(String email) {
		
		// 랜덤키
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		mDao.updateKey(sb.toString(), email);
		return (String)sb.toString(); 
	}
	
	@Override
	public boolean isValidCode(String key) {
		return mDao.isValidCode(key);
	}

	@Override
	public void addMember(String email, String pw) {
		mDao.addMember(email, pw);
	}

	@Override
	public void modifyProfileImg(int memberId, String profileImg) {
		mDao.modifyProfileImg(memberId, profileImg);
	}


	@Override
	public void modifyPw(int memberId, String pw) {
		mDao.modifyPw(memberId, pw);
	}
	
	@Override
	public void modifyInfo(int memberId, String email, String nickName) {
		mDao.modifyEmail(memberId, email);
		mDao.modifyNickName(memberId, nickName);
	}
	
	@Override
	public List<Map<String, Object>> getMyBoard(int memberId, int page) {
		int end = 10 * page;
		int start = end - 9;
		return mDao.getMyBoard(memberId, start, end);
	}

	@Override
	public List<Map<String, Object>> getLikedBoard(int memberId, int page) {
		int end = 10 * page;
		int start = end - 9;
		return mDao.getLikedBoard(memberId, start, end);
	}

	@Override
	public List<Map<String, Object>> getCommentBoard(int memberId, int page) {
		int end = 10 * page;
		int start = end - 9;
		return mDao.getCommentBoard(memberId, start, end);
	}

	@Override
	public Map<String,Object> getMyPage(int memberId) {
		
		Map<String,Object> map = new HashMap<>();
		map.put("getMemberProfile", mDao.getMemberProfile(memberId));
		map.put("getMyBoard", mDao.getMyBoard(memberId, 1, 10));
		map.put("getLikedBoard", mDao.getLikedBoard(memberId, 1, 10));
		map.put("getCommentBoard", mDao.getCommentBoard(memberId, 1, 10));
		
		return map;
	}

	@Override
	public String getPw(int memberId) {
		return mDao.getpw(memberId);
	}

	
}
