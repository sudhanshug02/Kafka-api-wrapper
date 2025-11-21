package com.example.kafkaap.wrapper.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {
    public static boolean validateToken(String token, String secret) {
        try {
            if (token == null) return false;
            if (token.startsWith("Bearer ")) token = token.substring(7);
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
