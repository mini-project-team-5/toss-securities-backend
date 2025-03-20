package com.tossclone.securities.util;

import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "my-secret-key-for-jwt-token";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;


    public String generateToken(String userId) {
        long now = System.currentTimeMillis();
        long exp = now + EXPIRATION_TIME;

        String header = base64Encode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64Encode("{\"sub\":\"" + userId + "\",\"iat\":" + now + ",\"exp\":" + exp + "}");

        String signature = hmacSha256(header + "." + payload, SECRET_KEY);

        return header + "." + payload + "." + signature;
    }


    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String signature = hmacSha256(parts[0] + "." + parts[1], SECRET_KEY);
            return signature.equals(parts[2]);
        } catch (Exception e) {
            return false;
        }
    }


    public String extractUserId(String token) {
        try {
            String payload = new String(Base64.getDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
            return payload.split("\"sub\":\"")[1].split("\"")[0];
        } catch (Exception e) {
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
