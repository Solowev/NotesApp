package com.example.mainserver.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Value("${jwt.expiration}")
    private int expirationTime;

    public String generateToken(String username){
        return Jwts.builder()
                .setClaims(Map.of("username", username))
                .setIssuedAt(new Date())
                .setExpiration(new Date(((new Date().getTime() + expirationTime))))
                .signWith(Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.ES512)
                .compact();
    }

    public String getUsernameFromToken(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return String.valueOf(claims.get("username"));
        } catch(JwtException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
