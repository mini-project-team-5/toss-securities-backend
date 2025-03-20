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

import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
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
        
        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "요청 데이터가 비어 있습니다!"));
        }

        String name = (String) requestBody.get("name");
        String birth_date = (String) requestBody.get("birth_date");
        String phone_number = (String) requestBody.get("phone_number");
        String carrier = (String) requestBody.get("carrier");
        String verification_code = (String) requestBody.get("verification_code");
        
        System.out.println("📌 name: " + name + ", birth_date: " + birth_date + ", phone_number: " + phone_number);

        if (name == null || name.isEmpty()) return ResponseEntity.badRequest().body("이름을 입력해주세요!");
        if (birth_date == null || birth_date.length() != 7) return ResponseEntity.badRequest().body("생년월일(YYMMDD-G)을 올바르게 입력해주세요!");
        if (phone_number == null || phone_number.isEmpty()) return ResponseEntity.badRequest().body("휴대폰 번호를 입력해주세요!");
        if (carrier == null || carrier.isEmpty()) return ResponseEntity.badRequest().body("통신사를 선택해주세요!");
        if (verification_code == null || verification_code.isEmpty()) return ResponseEntity.badRequest().body("인증번호를 입력해주세요!");

        boolean isVerified = smsService.verifyCode(phone_number, verification_code);
        if (!isVerified) {
            return ResponseEntity.badRequest().body(Map.of("message", "인증번호가 일치하지 않습니다!"));
        }

        Member member = new Member(null, name, birth_date, phone_number, carrier);
        authService.register(member);
        
        System.out.println("✅ 회원가입 완료: " + member.getUser_id());

        return ResponseEntity.ok(Map.of(
            "message", "회원가입 성공!",
            "name", member.getName()
        ));
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

        
        Long user_id = member.getUser_id();
        String token = jwtUtil.generateToken(user_id);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt_token", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(60 * 60 * 24)
            .build();
        
        System.out.println("토큰 생성 완료: " + token);

        authService.saveToken(user_id, token);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(Map.of("message", "로그인 성공!", "token", token, "name", member.getName()));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "jwt_token", defaultValue = "") String token) {
        System.out.println("로그아웃 요청 받음 - 토큰: " + token);

        if (token.isEmpty()) {
            return ResponseEntity.badRequest().body("유효한 로그인 상태가 아닙니다.");
        }

        Long user_id = jwtUtil.extractUserId(token);
        if (user_id != null) {
            authService.deleteToken(user_id);
        }

        ResponseCookie expiredCookie = ResponseCookie.from("jwt_token", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)  // 즉시 만료
            .build();

        System.out.println("로그아웃 완료 - DB 토큰 삭제 및 쿠키 무효화");

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
            .body("로그아웃 성공!");
    }

    //토큰 테스트 (지워도 됨)
    @GetMapping("/test-expired-token")
    public ResponseEntity<?> testExpiredToken(@CookieValue(value = "jwt_token", defaultValue = "") String token) {
        System.out.println("[토큰 만료 테스트] 받은 토큰: " + token);

        if (token == null || token.isEmpty()) {
            System.out.println(" 토큰이 비어있습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" 토큰이 비어있습니다.");
        }

        if (!authService.isTokenValid(token)) {
            System.out.println(" 토큰이 만료되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" 토큰이 만료되었습니다.");
        }

        System.out.println(" 토큰이 아직 유효합니다.");
        return ResponseEntity.ok("토큰이 아직 유효합니다.");
    }
}