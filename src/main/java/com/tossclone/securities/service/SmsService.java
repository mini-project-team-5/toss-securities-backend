package com.tossclone.securities.service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SmsService {
    @Value("${coolsms.api-key}")
    private String apiKey;

    @Value("${coolsms.api-secret}")
    private String apiSecret;

    @Value("${coolsms.sender-phone}")
    private String senderPhone;

    private DefaultMessageService messageService;

    // 🔹 인증번호 저장소 (key: 전화번호, value: 인증번호)
    private final Map<String, String> verificationCodes = new HashMap<>();

    @PostConstruct
    public void init() {
        System.out.println("📌 CoolSMS API Key: " + apiKey);
        System.out.println("📌 CoolSMS API Secret: " + apiSecret);
        System.out.println("📌 CoolSMS Sender Phone: " + senderPhone);

        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    /**
     * 🔹 휴대폰 번호로 인증번호 전송
     */
    public String sendSms(String phoneNumber) {
        String authCode = generateAuthCode();

        Message message = new Message();
        message.setFrom(senderPhone);
        message.setTo(phoneNumber);
        message.setText("[토스 클론] 인증번호: " + authCode);

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));

            // 🔹 인증번호 저장 (전화번호 → 인증번호 매핑)
            verificationCodes.put(phoneNumber, authCode);

            System.out.println("📌 인증번호가 발송되었습니다! [" + authCode + "]");
            return "인증번호가 발송되었습니다!";
        } catch (Exception e) {
            return "문자 전송 실패: " + e.getMessage();
        }
    }

    /**
     * 🔹 사용자가 입력한 인증번호 검증
     */
    public boolean verifyCode(String phoneNumber, String inputCode) {
        String correctCode = verificationCodes.get(phoneNumber);

        // 🔹 인증번호 검증 후 삭제 (1회성)
        if (correctCode != null && correctCode.equals(inputCode)) {
            verificationCodes.remove(phoneNumber);
            return true;
        }
        return false;
    }

    /**
     * 🔹 6자리 랜덤 인증번호 생성
     */
    private String generateAuthCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
//    public String sendSms(String phoneNumber) {
//        System.out.println("[TEST MODE] 인증번호 발송 요청됨: " + phoneNumber);
//
//        // 실제 문자 전송 대신 테스트용 응답 반환
//        return "[TEST MODE] 인증번호가 성공적으로 발송된 것으로 가정합니다. (코드: 123456)";
//    }
//
//    /**
//     * ✅ 실제 인증번호 대신 "123456"을 사용하여 검증 테스트 가능
//     */
//    public boolean verifyCode(String phoneNumber, String verificationCode) {
//        System.out.println("[TEST MODE] 인증번호 검증 요청됨: " + phoneNumber + " 입력된 코드: " + verificationCode);
//
//        // 실제 DB 조회 없이 "123456" 입력 시 테스트 성공 처리
//        return "123456".equals(verificationCode);
//    }
//}