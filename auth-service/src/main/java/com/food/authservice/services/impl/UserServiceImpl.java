package com.food.authservice.services.impl;

import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;
import com.food.authservice.exceptions.HttpError;
import com.food.authservice.repositorie.TokenRepository;
import com.food.authservice.repositorie.UserRepository;
import com.food.authservice.services.contract.UserService;
import com.food.authservice.utils.JwtTools;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTools jwtTools;
    private final TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository, JwtTools jwtTools, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.jwtTools = jwtTools;
        this.tokenRepository = tokenRepository;
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
            String tokenString = jwtTools.generateToken(user);
            Token token = new Token(user, tokenString);

            return tokenRepository.save(token);
        }catch (HttpError e) {
            throw e;
        }
    }

    @Override
    public void cleanTokens(User user) {
        try{
            List<Token> tokens = tokenRepository.findByUserAndIsValid(user, true);
            tokens.forEach(t ->{
                if(!jwtTools.verifyToken(t.getToken())) {
                    t.setIsValid(false);
                    tokenRepository.save(t);
                }
            });
        }catch (HttpError e) {
            throw e;
        }
    }
}
