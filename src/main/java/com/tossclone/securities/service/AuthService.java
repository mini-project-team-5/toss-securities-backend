package com.tossclone.securities.service;

import com.tossclone.securities.dao.LoginDao;
import com.tossclone.securities.dao.MemberDao;
import com.tossclone.securities.dto.Member;
import com.tossclone.securities.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public void saveToken(Long user_id, String token) {
        System.out.println("🔹 saveToken 실행됨 - user_id: " + user_id + ", token: " + token);
        loginDao.saveToken(user_id, token);
        System.out.println("✅ saveToken 실행 완료!");
    }


    public boolean isTokenValid(String token) {

        if (jwtUtil.isTokenExpired(token)) {
            System.out.println("토큰이 만료되었습니다.");
            return false;
        }

        Long user_id = jwtUtil.extractUserId(token);

        if (user_id <= 0) {
            System.out.println("유효하지 않은 userId: " + user_id);
            return false;
        }
        
        String storedToken = loginDao.findTokenByUserId(user_id);

        if (storedToken == null || !storedToken.equals(token)) {
            System.out.println("유효하지 않은 토큰입니다.");
            return false;
        }

        return true;
    }
}
