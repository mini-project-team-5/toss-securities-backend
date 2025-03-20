package com.tossclone.securities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tossclone.securities.dao.MemberDao;

@Service
public class MemberService {
	@Autowired MemberDao memberDao;
	public String getUserNameByUserId(Long userId) {
		return memberDao.findUserNameByUserId(userId);
	}

}
