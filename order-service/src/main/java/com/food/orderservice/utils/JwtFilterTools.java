package com.food.orderservice.utils;

import com.food.orderservice.domain.dto.auth.UserDto;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilterTools extends OncePerRequestFilter {

    private final JwtTools jwtTools;

    public JwtFilterTools(JwtTools jwtTools) {
        this.jwtTools = jwtTools;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerToken = request.getHeader("Authorization");
        String token = null;
        UserDto user = null;

        if(headerToken != null && headerToken.startsWith("Bearer ") && headerToken.length() > 7){
            token = headerToken.substring(7);
            try{
                user = jwtTools.getUserFromToken(token);
                System.out.println(user);
                if(user != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    Boolean isValid = jwtTools.isValid(token);
                    if(isValid){
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }else {
                    System.out.println("Invalid JWT token");
                }
            }catch (IllegalArgumentException e) {
                System.out.println("Invalid JWT token");
            } catch (MalformedJwtException e) {
                System.out.println("Malformed JWT token");
            } catch (Exception e) {
                System.out.println("An error occurred while processing the JWT token: " + e.getMessage());
            }
        }else {
            System.out.println("Authorization header is missing or does not start with Bearer");
        }
        filterChain.doFilter(request, response);
    }
}
