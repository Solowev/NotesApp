package com.example.mainserver.Authentication.Filters;

import com.example.mainserver.Authentication.Models.ERole;
import com.example.mainserver.Authentication.Models.Role;
import com.example.mainserver.Authentication.Proxy.AuthenticationServerProxy;
import com.example.mainserver.Authentication.UsernamePasswordAuthentication;
import com.example.mainserver.Utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Log
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Autowired
    private AuthenticationServerProxy proxy;

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Doing jwt Filter");

        String jwt = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromToken(jwt);

        Set<GrantedAuthority> roles= proxy.getUserRoles(username).stream()
                .map(role->new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toSet());

        Authentication auth = new UsernamePasswordAuthentication(username, null, roles);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login");
    }
}
