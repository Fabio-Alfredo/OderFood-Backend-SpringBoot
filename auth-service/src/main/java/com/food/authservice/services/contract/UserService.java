package com.food.authservice.services.contract;

import com.food.authservice.domains.dtos.RegisterDto;
import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;

public interface UserService {
    User findByIdentifier(String identifier);
    User findByEmail(String email);
    User registerUser(RegisterDto registerDto);

    Token registerToken(User user);
    void cleanTokens(User user);
}
