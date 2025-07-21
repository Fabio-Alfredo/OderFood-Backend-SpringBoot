package com.food.authservice.services.contract;

import com.food.authservice.domains.models.PasswordRecoveryToken;
import com.food.authservice.domains.models.User;

public interface TokenRecoveryServices {
    PasswordRecoveryToken registerToken(User user);
    String getEmailByToken(String token);
    Boolean validTokenRecovery(User user, String token);
}

