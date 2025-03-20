package com.tossclone.securities.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tossclone.securities.dao.CommunityDao;
import com.tossclone.securities.dto.Comment;


@Service
public class CommunityService {
	@Autowired
	CommunityDao communityDao;
	
	public List<Comment> getComments(String stockCode) throws Exception {
		return communityDao.getComments(stockCode);
	}

	public void createComment(Comment comment) throws Exception {
		communityDao.createComment(comment);
	}

}
