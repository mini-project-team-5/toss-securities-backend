package com.tossclone.securities.util;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "my-secret-key-for-jwt-token";
    private static final long EXPIRATION_TIME = 1000 * 60 * 30; // 30분
//    private static final long EXPIRATION_TIME = 60 * 1000; // 60초로 변경

    public String generateToken(Long user_id) {  
        long now = System.currentTimeMillis();
        long exp = now + EXPIRATION_TIME;

        String header = base64Encode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64Encode("{\"sub\":\"" + user_id + "\",\"iat\":" + now + ",\"exp\":" + exp + "}");

        String signature = hmacSha256(header + "." + payload, SECRET_KEY);
        String token = header + "." + payload + "." + signature;

        System.out.println("✅ 생성된 JWT 토큰: " + token);
        return token;
    }


    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String signature = hmacSha256(parts[0] + "." + parts[1], SECRET_KEY);
            if (!signature.equals(parts[2])) return false;

            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            String payloadJson = new String(Base64.getDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);

            Long exp = payload.get("exp") != null ? ((Number) payload.get("exp")).longValue() : null;
            if (exp == null) {
                System.out.println("만료 시간 정보 없음! 토큰 만료로 처리됨.");
                return true;
            }
            boolean isExpired = exp < System.currentTimeMillis();
            System.out.println("토큰 만료 여부: " + isExpired);
            return isExpired;
        } catch (Exception e) {
            System.out.println("토큰 만료 검사 중 오류 발생! 만료 처리됨.");
            return true;
        }
    }

    public Long extractUserId(String token) {
        try {
            String payloadJson = new String(Base64.getDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);

            Long user_id = payload.get("sub") != null ? Long.valueOf(payload.get("sub").toString()) : null;
            System.out.println("추출된 userId: " + user_id);
            return user_id;
        } catch (Exception e) {
            System.out.println("userId 추출 실패! 반환값: null");
            return null;
        }
    }

    private String base64Encode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secretKey);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256 서명 생성 실패", e);
        }
    }
}
