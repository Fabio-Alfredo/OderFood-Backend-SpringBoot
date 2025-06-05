package com.food.menuservice.utils;

import com.food.menuservice.domain.dto.auth.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtTools {

    @Value("${jwt.secret}")
    private String secret;

    public Boolean isValid(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build();
            parser.parse(token);

            return true;
        }catch (Exception e) {
            return false;
        }
    }


    public  String getEmailFromToken(String token){
        try {
         JwtParser parser = Jwts.parser()
                 .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                 .build();

            return parser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }catch (Exception e) {
            return null;
        }
    }

    public UserDto getUserFromToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build();
//            System.out.println("email"+getEmailFromToken(token));
            Claims claims = parser.parseSignedClaims(token).getPayload();
            UserDto userDto = new UserDto();
            userDto.setEmail(claims.get("email", String.class));
            userDto.setUsername(claims.get("username", String.class));
            userDto.setId(UUID.fromString(claims.get("id", String.class)));

            return userDto ;
        }catch (Exception e) {
            return null;
        }
    }
}
