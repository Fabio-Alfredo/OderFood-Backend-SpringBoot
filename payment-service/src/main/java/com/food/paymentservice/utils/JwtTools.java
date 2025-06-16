package com.food.paymentservice.utils;

import com.food.paymentservice.domain.dtos.auth.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class JwtTools {

    @Value("${jwt.secret}")
    private String secretKey;

    public Boolean isValidToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();
            parser.parse(token);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public UserDto validateAndGetUserFromToken(String token){
        try{
           JwtParser parser = Jwts.parser()
                   .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                   .build();

           var claims = parser.parseSignedClaims(token).getPayload();
           UserDto userDto = new UserDto();
           userDto.setEmail(claims.get("email", String.class));
           userDto.setUsername(claims.get("username", String.class));
           userDto.setId(UUID.fromString(claims.get("id", String.class)));

           return userDto;
        }catch (Exception e) {
            return null;
        }
    }

}
