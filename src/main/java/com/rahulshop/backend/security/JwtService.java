package com.rahulshop.backend.security;

import com.rahulshop.backend.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    // JWT ke liye secret key
    private static final String SECRET =
            "UmFodWwtU2hvcC1KV1QtU2VjcmV0LUtleS0yMDI2LVByYWN0aWNl";

    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

    // JWT Token generate karega
    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
                )
                .signWith(secretKey)
                .compact();
    }

    // Token se email nikalna
    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}