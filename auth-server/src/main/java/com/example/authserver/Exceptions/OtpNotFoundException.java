package com.example.authserver.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class OtpNotFoundException extends AuthenticationException {

    public OtpNotFoundException(String username){
        super("Couldn't find otp for user: " + username);
    }

    public OtpNotFoundException(String message, Throwable t){
        super(message, t);
    }
}
