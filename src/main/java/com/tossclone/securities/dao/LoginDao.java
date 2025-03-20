package com.tossclone.securities.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginDao {
    @Insert("INSERT INTO login_token (user_id, token, expire_time) VALUES (#{user_id}, #{token}, DATE_ADD(NOW(), INTERVAL 30 MINUTE)) " +
            "ON DUPLICATE KEY UPDATE token = #{token}, logintime = NOW(), expire_time = DATE_ADD(NOW(), INTERVAL 30 MINUTE)")
    void saveToken(@Param("user_id") Long user_id, @Param("token") String token);

    @Select("SELECT token FROM login_token WHERE user_id = #{user_id}")
    String findTokenByUserId(@Param("user_id") Long user_id);
    
    @Delete("DELETE FROM login_token WHERE user_id = #{user_id}")
    void deleteToken(@Param("user_id") Long user_id);
    
    @Delete("DELETE FROM login_token WHERE expire_time <= NOW()")
    void deleteExpiredTokens();
}
