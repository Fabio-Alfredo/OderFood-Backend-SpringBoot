package com.food.authservice.services.impl;

import com.food.authservice.domains.models.PasswordRecoveryToken;
import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;
import com.food.authservice.repositorie.TokenRecoveryRepository;
import com.food.authservice.services.contract.TokenRecoveryServices;
import com.food.authservice.utils.RecoveryJwtTools;
import org.springframework.stereotype.Service;

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
}
