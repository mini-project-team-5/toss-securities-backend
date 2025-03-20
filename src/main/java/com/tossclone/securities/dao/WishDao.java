package com.tossclone.securities.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tossclone.securities.dto.Wish;

@Mapper
public interface WishDao {

	public List<Wish> getWishList(Long userId) throws Exception;

	public void addWish(Wish wish) throws Exception;

	public void deleteWish(Long userId, String stockCode) throws Exception;

}
