package com.food.paymentservice.services.impl;

import com.food.paymentservice.Exceptions.HttpError;
import com.food.paymentservice.domain.dtos.auth.UserDto;
import com.food.paymentservice.services.contrat.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public UserDto getUserAuthenticated() {
        try{
            UserDto user = (UserDto) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            if(user == null) {
                throw new HttpError(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }
            return user;
        }catch (HttpError e) {
            throw new HttpError(e.getHttpStatus(), e.getMessage());
        }
    }
}
