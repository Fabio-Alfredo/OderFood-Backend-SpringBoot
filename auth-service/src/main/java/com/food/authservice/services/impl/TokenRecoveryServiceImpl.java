package com.food.authservice.services.impl;

import com.food.authservice.domains.models.PasswordRecoveryToken;
import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;
import com.food.authservice.exceptions.HttpError;
import com.food.authservice.repositorie.TokenRecoveryRepository;
import com.food.authservice.services.contract.TokenRecoveryServices;
import com.food.authservice.utils.RecoveryJwtTools;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenRecoveryServiceImpl implements TokenRecoveryServices {

    private final RecoveryJwtTools recoveryJwtTools;
    private final TokenRecoveryRepository tokenRecoveryRepository;

    public TokenRecoveryServiceImpl(RecoveryJwtTools recoveryJwtTools, TokenRecoveryRepository tokenRecoveryRepository) {
        this.recoveryJwtTools = recoveryJwtTools;
        this.tokenRecoveryRepository = tokenRecoveryRepository;
    }

    @Override
    public PasswordRecoveryToken registerToken(User user) {
        try{
            String tokenString = recoveryJwtTools.generateRecoveryToken(user);
            PasswordRecoveryToken token = new PasswordRecoveryToken();
            token.setToken(tokenString);
            token.setUser(user);

            return tokenRecoveryRepository.save(token);
        }catch (Exception e){
            throw  e;
        }
    }

    @Override
    public String getEmailByToken(String token) {
        try{
            String email = recoveryJwtTools.getEmailFromTokenRecovery(token);
            if(email == null)
                throw new HttpError(HttpStatus.NOT_FOUND, "Invalid token by recovery password");

            return email;
        }catch (HttpError e){
            throw e;
        }
    }


    @Override
    @Transactional
    public Boolean validTokenRecovery(User user, String token) {
        PasswordRecoveryToken tokenEntity = tokenRecoveryRepository
                .findByUserAndTokenAndCanActive(user, token, true)
                .orElseThrow(() -> new HttpError(HttpStatus.UNAUTHORIZED, "Token not valid"));

        tokenEntity.setCanActive(false);
        tokenRecoveryRepository.save(tokenEntity);

        return true;
    }



}
