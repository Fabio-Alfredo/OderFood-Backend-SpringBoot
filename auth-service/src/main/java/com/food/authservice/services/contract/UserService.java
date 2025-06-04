package com.food.authservice.services.contract;

import com.food.authservice.domains.dtos.user.LoginDto;
import com.food.authservice.domains.dtos.user.RegisterDto;
import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;

public interface UserService {
    User findByIdentifier(String identifier);
    User findByEmail(String email);
    User registerUser(RegisterDto registerDto);
    Token loginUser(LoginDto loginDto);

    Token registerToken(User user);
    void cleanTokens(User user);
}
