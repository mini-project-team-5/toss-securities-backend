package com.tossclone.securities.dao;

import com.tossclone.securities.dto.Member;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberDao {
    @Insert("INSERT INTO members (name, birth_date, phone_number, carrier) VALUES (#{name}, #{birth_date}, #{phone_number}, #{carrier})")
    void register(Member member);

    @Select("SELECT * FROM members WHERE name = #{name} AND phone_number = #{phone_number} AND SUBSTRING(birth_date, 1, 6) = #{birth_date}")
    Member findByNamePhoneAndBirth(
        @Param("name") String name,
        @Param("phone_number") String phone_number,
        @Param("birth_date") String birth_date
    );

}
