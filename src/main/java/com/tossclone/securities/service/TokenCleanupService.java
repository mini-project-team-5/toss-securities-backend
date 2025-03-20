package com.tossclone.securities.service;

import com.tossclone.securities.dao.LoginDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupService {
    private final LoginDao loginDao;

    public TokenCleanupService(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    // ✅ 1분마다 만료된 토큰 삭제 (1000ms * 60 = 1분)
    @Scheduled(fixedRate = 60000)
    public void removeExpiredTokens() {
        System.out.println("만료된 토큰 삭제 작업 실행...");
        loginDao.deleteExpiredTokens();
        System.out.println("만료된 토큰 삭제 완료!");
    }
}
