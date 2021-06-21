package com.example.mainserver.Authentication.Providers;

import com.example.mainserver.Authentication.Models.User;
import com.example.mainserver.Authentication.Proxy.AuthenticationServerProxy;
import com.example.mainserver.Authentication.UsernamePasswordAuthentication;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Log
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthenticationServerProxy proxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Username provider");
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        if(proxy.sendAuth(username, password)) {
            return new UsernamePasswordAuthentication(username, password);
        }
        else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
