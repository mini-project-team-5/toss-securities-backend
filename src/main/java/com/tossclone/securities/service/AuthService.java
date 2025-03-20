package com.tossclone.securities.service;

import com.tossclone.securities.dao.LoginDao;
import com.tossclone.securities.dao.MemberDao;
import com.tossclone.securities.dto.Member;
import com.tossclone.securities.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private JwtUtil jwtUtil;

    public void register(Member member) {
        memberDao.register(member);
    }

    public Member login(String name, String phone_number, String birth_date) {
        return memberDao.findByNamePhoneAndBirth(name, phone_number, birth_date);
    }

    public void saveToken(String userId, String token) {
        System.out.println("🔹 토큰 저장: " + token);
        loginDao.saveToken(userId, token);
    }

    public boolean isTokenValid(String token) {
        String userId = jwtUtil.extractUserId(token);
        String storedToken = loginDao.findTokenByUserId(userId);
        return storedToken != null && storedToken.equals(token);
    }
}
