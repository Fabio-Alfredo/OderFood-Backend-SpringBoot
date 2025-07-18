package com.food.authservice.services.contract;

import com.food.authservice.domains.dtos.user.LoginDto;
import com.food.authservice.domains.dtos.user.RegisterDto;
import com.food.authservice.domains.dtos.user.UserTokenDto;
import com.food.authservice.domains.models.PasswordRecoveryToken;
import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;

import java.util.List;

public interface UserService {
    User findByIdentifier(String identifier);
    User findByEmail(String email);
    void registerUser(RegisterDto registerDto);
    Token loginUser(LoginDto loginDto);
    List<User>findAllUsers();

    //Recovery password
    PasswordRecoveryToken recoveryPassword(String email);


    //metodos para manejo de tokens
    Token registerToken(User user);
    void cleanTokens(User user);
    User getAuthenticatedUser();
}
