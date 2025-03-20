package com.tossclone.securities.dto;

import jakarta.validation.constraints.NotNull;

public class Member {
    private Long user_id;

    @NotNull(message = "이름을 입력해주세요!")
    private String name;

    @NotNull(message = "생년월일을 입력해주세요! (YYMMDD-G)")
    private String birth_date;

    @NotNull(message = "휴대폰 번호를 입력해주세요!")
    private String phone_number;

    @NotNull(message = "통신사를 선택해주세요!")
    private String carrier;

    public Member() {
    }

    public Member(Long user_id, String name, String birth_date, String phone_number, String carrier) {
        this.user_id = user_id;
        this.name = name;
        this.birth_date = birth_date;
        this.phone_number = phone_number;
        this.carrier = carrier;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
}
