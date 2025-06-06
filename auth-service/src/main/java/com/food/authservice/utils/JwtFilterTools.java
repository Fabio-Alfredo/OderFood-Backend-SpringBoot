package com.food.authservice.utils;

import com.food.authservice.domains.dtos.user.UserTokenDto;
import com.food.authservice.services.contract.UserService;
import io.jsonwebtoken.ExpiredJwtException;
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

    public JwtFilterTools(UserService userService, JwtTools jwtTools) {
        this.jwtTools = jwtTools;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && authorizationHeader.length() > 7) {
            token = authorizationHeader.substring(7);
           try{
               email = jwtTools.getEmailFromToken(token);
           }catch (IllegalArgumentException e){
               System.out.println("Invalid JWT token");
           }catch (MalformedJwtException e) {
               System.out.println("Malformed JWT token");
           }catch (ExpiredJwtException e){
                System.out.println("JWT token has expired");
           }
        }else{
            System.out.println("Authorization header is missing or does not start with Bearer");
        }

        if(email != null && token != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserTokenDto user = jwtTools.getUserFromToken(token);
            if(user!= null){
                Boolean isValid = jwtTools.verifyToken(token);
                if(isValid){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, null);
                     authToken.setDetails(
                             new WebAuthenticationDetailsSource().buildDetails(request)
                     );

                     SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }else {
            System.out.println("Email or token is null, or authentication is already set");
        }
        filterChain.doFilter(request, response);
    }
}
