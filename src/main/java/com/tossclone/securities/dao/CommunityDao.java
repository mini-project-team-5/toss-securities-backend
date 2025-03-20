package com.tossclone.securities.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import com.tossclone.securities.dto.Comment;

@Mapper
public interface CommunityDao {
	@Insert("INSERT INTO comment (stock_code, user_id, user_name,\n"
			+ "		content)\n"
			+ "		VALUES (#{stockCode},\n"
			+ "		#{userId},\n"
			+ "		#{userName},\n"
			+ "		#{content}\n"
			+ "		)")
	public void createComment(Comment comment) throws Exception;
	
	@Select("SELECT * FROM comment where stock_code = #{stockCode}")
	@Results({
	    @Result(property = "commentId", column = "comment_id"),
	    @Result(property = "stockCode", column = "stock_code"),
	    @Result(property = "userId", column = "user_id"),
	    @Result(property = "userName", column = "user_name"),
	    @Result(property = "content", column = "content"),
	})
	public List<Comment> getComments(String stockCode) throws Exception;
}
