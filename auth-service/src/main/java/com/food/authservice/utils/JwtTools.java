package com.food.authservice.utils;

import com.food.authservice.domains.models.User;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtTools {
    @Value("${jwt.secret}")
    private String secret_authentication;
    @Value("${jwt.expiration}")
    private Long expiration_authentication;

    @Value("${recovery.token.expiration}")
    private String secret_recovery;
    @Value("${recovery.token.expiration}")
    private Long expiration_recovery;

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        List<String>roles = user.getRoles().stream().map(role -> role.getId()).toList();
        claims.put("roles", roles);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (expiration_authentication) * 1000L))
                .signWith(Keys.hmacShaKeyFor( secret_authentication.getBytes()))
                .compact();
    }

    public String generateRecoveryToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("Type", "recovery_password");

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration_recovery *1000L))
                .signWith(Keys.hmacShaKeyFor(secret_recovery.getBytes()))
                .compact();
    }

    public Boolean verifyRecoveryToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret_recovery.getBytes()))
                    .build();

            var claims = parser.parseSignedClaims(token).getPayload();
            String type = claims.get("Type", String.class);

            return "recovery_password".equals(type);
        }catch (Exception e){
            return false;
        }
    }

    public String getEmailFromTokenRecovery(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret_recovery.getBytes()))
                    .build();
            return  parser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }catch (Exception e){
            return  null;
        }
    }


    public Boolean verifyToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret_authentication.getBytes()))
                    .build();

            parser.parse(token);

            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token){
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret_authentication.getBytes()))
                    .build();

            return parser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

}
