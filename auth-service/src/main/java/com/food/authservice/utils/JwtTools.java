package com.food.authservice.utils;

import com.food.authservice.domains.dtos.user.UserTokenDto;
import com.food.authservice.domains.enums.TokenType;
import com.food.authservice.domains.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTools {
    @Value("${jwt.secret}")
    private String secret_authentication;
    @Value("${jwt.expiration}")
    private Long expiration_authentication;

    @Value("${recory.token.secret}")
    private String secret_recovery;
    @Value("${recovery.token.expiration}")
    private Long expiration_recovery;

    public String generateToken(User user, TokenType tokenType){
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("id", user.getId());
//        claims.put("roles", user.getRoles());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (tokenType == TokenType.AUTHENTICATION ? expiration_authentication : expiration_recovery) * 1000L))
                .signWith(Keys.hmacShaKeyFor(tokenType == TokenType.AUTHENTICATION ? secret_authentication.getBytes() : secret_recovery.getBytes()))
                .compact();
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

    public UserTokenDto getUserFromToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret_authentication.getBytes()))
                    .build();

            Claims claims = parser.parseSignedClaims(token).getPayload();
            System.out.println(claims + " claims");
            UserTokenDto userTokenDto = new UserTokenDto();
            userTokenDto.setEmail(claims.get("email", String.class));
            userTokenDto.setUsername(claims.get("username", String.class));
            userTokenDto.setId(UUID.fromString(claims.get("id", String.class)));

            return userTokenDto;
        }catch (Exception e) {
            return null;
        }
    }
}
