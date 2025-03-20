package com.tossclone.securities.controller;

import com.tossclone.securities.dto.Member;
import com.tossclone.securities.service.AuthService;
import com.tossclone.securities.service.SmsService;
import com.tossclone.securities.service.VerificationService;
import com.tossclone.securities.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseCookie;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private SmsService smsService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private VerificationService verificationService;

    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestBody Map<String, String> requestBody) {
        String phone_number = requestBody.get("phone_number");

        if (phone_number == null || phone_number.isEmpty()) {
            return ResponseEntity.badRequest().body("휴대폰 번호를 입력해주세요!");
        }

        String responseMessage = smsService.sendSms(phone_number);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> requestBody) {
        String phone_number = requestBody.get("phone_number");
        String verification_code = requestBody.get("verification_code");

        if (phone_number == null || phone_number.isEmpty()) {
            return ResponseEntity.badRequest().body("휴대폰 번호를 입력해주세요!");
        }
        if (verification_code == null || verification_code.isEmpty()) {
            return ResponseEntity.badRequest().body("인증번호를 입력해주세요!");
        }

        boolean isVerified = smsService.verifyCode(phone_number, verification_code);
        if (isVerified) {
            verificationService.markVerified(phone_number);
            return ResponseEntity.ok("인증 성공!");
        } else {
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> requestBody) {
        System.out.println("회원가입 요청: " + requestBody);

        String name = (String) requestBody.get("name");
        String birth_date = (String) requestBody.get("birth_date");
        String phone_number = (String) requestBody.get("phone_number");
        String carrier = (String) requestBody.get("carrier");
        String verification_code = (String) requestBody.get("verification_code");

        if (name == null || name.isEmpty()) return ResponseEntity.badRequest().body("이름을 입력해주세요!");
        if (birth_date == null || birth_date.length() != 7) return ResponseEntity.badRequest().body("생년월일(YYMMDD-G)을 올바르게 입력해주세요!");
        if (phone_number == null || phone_number.isEmpty()) return ResponseEntity.badRequest().body("휴대폰 번호를 입력해주세요!");
        if (carrier == null || carrier.isEmpty()) return ResponseEntity.badRequest().body("통신사를 선택해주세요!");
        if (verification_code == null || verification_code.isEmpty()) return ResponseEntity.badRequest().body("인증번호를 입력해주세요!");

        boolean isVerified = smsService.verifyCode(phone_number, verification_code);
        if (!isVerified) {
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다!");
        }

        Member member = new Member(null, name, birth_date, phone_number, carrier);
        authService.register(member);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        String phone_number = requestBody.get("phone_number");
        String birth_date = requestBody.get("birth_date");

        System.out.println("[로그인 요청] 이름: " + name + ", 휴대폰: " + phone_number + ", 생년월일: " + birth_date);

        if (name == null || name.isEmpty()) return ResponseEntity.badRequest().body("이름을 입력해주세요!");
        if (phone_number == null || phone_number.isEmpty()) return ResponseEntity.badRequest().body("휴대폰 번호를 입력해주세요!");
        if (birth_date == null || birth_date.isEmpty()) return ResponseEntity.badRequest().body("생년월일을 입력해주세요!");

        Member member = authService.login(name, phone_number, birth_date);
        if (member == null) {
            System.out.println("로그인 실패: 회원 정보가 일치하지 않음");
            return ResponseEntity.badRequest().body("로그인 실패! 회원 정보를 확인하세요!");
        }

        if (!verificationService.isVerified(phone_number)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다. 인증 후 로그인하세요.");
        }

        String token = jwtUtil.generateToken(String.valueOf(member.getId()));

        ResponseCookie jwtCookie = ResponseCookie.from("jwt_token", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(60 * 60 * 24)
            .build();

        authService.saveToken(String.valueOf(member.getId()), token);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(Map.of("message", "로그인 성공!", "token", token, "name", member.getName()));
    }


    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@CookieValue(value = "jwt_token", defaultValue = "") String token) {
        System.out.println("[토큰 확인 요청] " + token);

        if (token.isEmpty() || !authService.isTokenValid(token)) {
            System.out.println("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" 유효하지 않은 토큰입니다.");
        }

        String userId = jwtUtil.extractUserId(token);
        System.out.println("로그인 상태 유지, 유저 ID: " + userId);

        return ResponseEntity.ok(Map.of("message", "로그인 상태 유지", "userId", userId));
    }
}