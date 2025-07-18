package com.food.authservice.utils;

import com.food.authservice.domains.models.User;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class RecoveryJwtTools {

    @Value("${recory.token.secret}")
    private String recovery_secret;
    @Value("${recovery.token.expiration}")
    private Long recovery_expiration;

    public String generateRecoveryToken(User user){
        Map<String, Object>claims = new HashMap<>();
        claims.put("type", "recovery_password");

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+recovery_expiration *1000L))
                .signWith(Keys.hmacShaKeyFor(recovery_secret.getBytes()))
                .compact();
    }

    public Boolean verifyTokenRecovery(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(recovery_secret.getBytes()))
                    .build();
            var claims = parser.parseSignedClaims(token).getPayload();
            String type = claims.get("type", String.class);

            return "recovery_token".equals(type);
        }catch (Exception e){
            return false;
        }
    }

    public String getEmailFromTokenRecovery(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(recovery_secret.getBytes()))
                    .build();

            return parser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }catch (Exception e){
            return  null;
        }
    }
}
