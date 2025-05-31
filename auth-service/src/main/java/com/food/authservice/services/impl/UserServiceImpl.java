package com.food.authservice.services.impl;

import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;
import com.food.authservice.services.contract.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User findByIdentifier(String identifier) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public Token registerToken(User user) {
        return null;
    }

    @Override
    public Boolean isTokenValid(User user, String token) {
        return null;
    }

    @Override
    public void cleanTokens(User user) {

    }
}
