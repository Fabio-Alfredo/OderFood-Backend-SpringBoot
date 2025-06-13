package com.food.orderservice.service.impl;

import com.food.orderservice.Exceptions.HttpError;
import com.food.orderservice.domain.dto.auth.UserDto;
import com.food.orderservice.service.contract.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public UserDto getUserAuthenticated() {
        try{
            UserDto user = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user == null) {
                throw new HttpError(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }

            return user;
        }catch (HttpError e) {
            throw e;
        }
    }
}
