package com.example.mainserver.Authentication.Providers;

import com.example.mainserver.Authentication.OtpAuthentication;
import com.example.mainserver.Authentication.Proxy.AuthenticationServerProxy;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Log
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthenticationServerProxy proxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("OTP Provider");
        String username = authentication.getName();
        String code = String.valueOf(authentication.getCredentials());
        if(proxy.sendOTP(username, code)){
            return new OtpAuthentication(username, code);
        } else{
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
