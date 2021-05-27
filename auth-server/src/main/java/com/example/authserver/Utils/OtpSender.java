package com.example.authserver.Utils;

public interface OtpSender {
    void sendOtp(String to, String code);
}
