package com.example.mainserver.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException{

    public TokenRefreshException(String token, String message){
        super(String.format("Failed! Token: %s\nmessage:%s", token, message));
    }
}
