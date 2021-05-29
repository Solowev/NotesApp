package com.example.authserver.Services;

public interface OtpService {
    void sendOtp(String to, String code);
}
