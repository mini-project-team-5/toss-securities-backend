package com.tossclone.securities.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginDao {
    @Insert("INSERT INTO login_token (user_id, token) VALUES (#{userId}, #{token}) ON DUPLICATE KEY UPDATE token = #{token}, logintime = NOW()")
    void saveToken(@Param("userId") String userId, @Param("token") String token);

    @Select("SELECT token FROM login_token WHERE user_id = #{userId}")
    String findTokenByUserId(@Param("userId") String userId);
}
