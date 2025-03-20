package com.tossclone.securities.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tossclone.securities.dto.Comment;

@Mapper
public interface CommunityDao {
	public void createComment(Comment comment) throws Exception;
	
	public List<Comment> getComments(String stockCode) throws Exception;
}
