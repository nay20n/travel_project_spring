package com.nh.service;

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
	public boolean isExistEmail(String email) {
		return mDao.isExistEmail(email);
	}

	@Override
	public boolean canLogin(String email, String pw) {
		return mDao.canLogin(email,pw);
	}

	@Override
	public void updateKey(String key, String email) {
		mDao.updateKey(key, email);
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
	public void modifyEmail(int memberId, String email) {
		mDao.modifyEmail(memberId, email);
	}

	@Override
	public void modifyPw(int memberId, String pw) {
		mDao.modifyPw(memberId, pw);
	}
}
