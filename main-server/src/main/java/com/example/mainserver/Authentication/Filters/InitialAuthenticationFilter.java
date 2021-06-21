package com.example.mainserver.Authentication.Filters;

import com.example.mainserver.Authentication.Models.ERole;
import com.example.mainserver.Authentication.Models.Role;
import com.example.mainserver.Authentication.OtpAuthentication;
import com.example.mainserver.Authentication.Proxy.AuthenticationServerProxy;
import com.example.mainserver.Authentication.UsernamePasswordAuthentication;
import com.example.mainserver.Utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

@Component
@Log
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Doing initial filter");

        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String code = request.getHeader("code");

        if(!StringUtils.hasText(code)){
            Authentication auth = new UsernamePasswordAuthentication(username, password);
            manager.authenticate(auth);
        } else {
            Authentication auth = new OtpAuthentication(username, code);
            manager.authenticate(auth);

            String jwt = jwtUtil.generateToken(username);
            response.setHeader("Authorization", jwt);
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login");
    }
}
