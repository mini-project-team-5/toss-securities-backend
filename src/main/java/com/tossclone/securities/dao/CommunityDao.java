package com.tossclone.securities.dao;

import org.apache.ibatis.annotations.Mapper;

import com.tossclone.securities.dto.Comment;

@Mapper
public interface CommunityDao {
	public void createComment(Comment comment) throws Exception;
}
