package com.tossclone.securities.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import com.tossclone.securities.dto.Wish;

@Mapper
public interface WishDao {

	@Select("SELECT * FROM wish WHERE user_id = #{userId}")
	@Results(id = "WishResultMap", value = {
	    @Result(property = "wishId", column = "wish_id"),
	    @Result(property = "userId", column = "user_id"),
	    @Result(property = "stock.code", column = "stock_code"),
	    @Result(property = "stock.name", column = "stock_name"),
	    @Result(property = "stock.rank", column = "stock_rank"),
	    @Result(property = "stock.price", column = "stock_price"),
	    @Result(property = "stock.volume", column = "stock_volume"),
	    @Result(property = "stock.rate", column = "stock_rate")
	})
	public List<Wish> getWishList(Long userId) throws Exception;


	@Insert("INSERT INTO wish (user_id, stock_code, stock_name, stock_rank, stock_price, stock_volume, stock_rate) VALUES (#{userId}, #{stock.code}, #{stock.name}, #{stock.rank}, #{stock.price}, #{stock.volume}, #{stock.rate})")
	public void addWish(Wish wish) throws Exception;

	@Delete("DELETE FROM wish WHERE user_id = #{userId} AND stock_code = #{stockCode}")
	public void deleteWish(Long userId, String stockCode) throws Exception;

}
