package com.tossclone.securities.dao;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.tossclone.securities.dto.Wish;

@Mapper
public interface WishDao {
	public void addWish(Wish wish) throws SQLException;
}
