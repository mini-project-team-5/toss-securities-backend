package com.tossclone.securities.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface SaltDao {
    @Insert("INSERT INTO login_salt (user_id, salt) VALUES (#{userId}, #{salt}) ON DUPLICATE KEY UPDATE salt = #{salt}")
    void saveSalt(@Param("userId") String userId, @Param("salt") String salt);

    @Select("SELECT salt FROM login_salt WHERE user_id = #{userId}")
    String findSaltByUserId(@Param("userId") String userId);
}
