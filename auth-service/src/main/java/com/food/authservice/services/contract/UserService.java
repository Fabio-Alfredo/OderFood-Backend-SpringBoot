package com.food.authservice.services.contract;

import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;

public interface UserService {
    User findByIdentifier(String identifier);
    User findByEmail(String email);

    Token registerToken(User user);
    void cleanTokens(User user);
}
