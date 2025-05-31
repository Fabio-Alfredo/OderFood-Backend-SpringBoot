package com.food.authservice.services.impl;

import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;
import com.food.authservice.exceptions.HttpError;
import com.food.authservice.repositorie.UserRepository;
import com.food.authservice.services.contract.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByIdentifier(String identifier) {
        try{
            User user = userRepository.findByEmailOrUsername(identifier, identifier);
            if (user == null) {
                throw new HttpError(HttpStatus.NOT_FOUND, "User not found with identifier: " + identifier);
            }
            return user;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new HttpError(HttpStatus.NOT_FOUND, "User not found with email: " + email);
            }
            return user;
        } catch (HttpError e) {
            throw e;
        }
    }

    @Override
    public Token registerToken(User user) {
        try{
            cleanTokens(user);
            //generar token
            Token token = new Token();
            return token;
        }catch (HttpError e) {
            throw e;
        }
    }

    @Override
    public Boolean isTokenValid(User user, String token) {
        return null;
    }

    @Override
    public void cleanTokens(User user) {

    }
}
