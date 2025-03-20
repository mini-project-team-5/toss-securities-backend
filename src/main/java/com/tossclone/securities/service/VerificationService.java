package com.tossclone.securities.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationService {
    private Map<String, Boolean> verifiedNumbers = new HashMap<>();

    public void markVerified(String phoneNumber) {
        verifiedNumbers.put(phoneNumber, true);
    }

    public boolean isVerified(String phoneNumber) {
        return verifiedNumbers.getOrDefault(phoneNumber, false);
    }
}

