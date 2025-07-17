package com.food.orderservice.utils;

import com.food.orderservice.domain.dto.auth.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTools {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Boolean isValid(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build();

            parser.parse(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public UserDto getUserFromToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build();
            Claims claims = parser.parseSignedClaims(token).getPayload();
            UserDto userDto = new UserDto();
            userDto.setEmail(claims.getSubject());
            userDto.setId(UUID.fromString(claims.get("id", String.class)));

            List<String> roles = ((List<?>) claims.get("roles"))
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());

            userDto.setRoles(roles);

            return userDto;
        }catch (Exception e) {
            return null;
        }
    }
}
