package com.food.authservice.controller;

import com.food.authservice.domains.dtos.GeneralResponse;
import com.food.authservice.domains.dtos.token.TokenDto;
import com.food.authservice.domains.dtos.user.EmailDto;
import com.food.authservice.domains.dtos.user.LoginDto;
import com.food.authservice.domains.dtos.user.RegisterDto;
import com.food.authservice.domains.models.PasswordRecoveryToken;
import com.food.authservice.domains.models.Token;
import com.food.authservice.exceptions.HttpError;
import com.food.authservice.services.contract.UserService;
import com.food.authservice.services.impl.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;

    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse>registerUser(@RequestBody RegisterDto registerDto) {
        try{
            userService.registerUser(registerDto);
            return GeneralResponse.getResponse(HttpStatus.ACCEPTED, "User registered successfully");
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

    @PostMapping("/login")
    public  ResponseEntity<GeneralResponse>loginUser(@RequestBody LoginDto loginDto) {
        try{
            Token token = userService.loginUser(loginDto);
            return GeneralResponse.getResponse(HttpStatus.OK, "User logged in successfully", new TokenDto(token));
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

    @PutMapping("/recover-password")
    public ResponseEntity<GeneralResponse>resetPassword(@RequestBody EmailDto email){
        try{

            PasswordRecoveryToken token = userService.recoveryPassword(email.getEmail());

            emailService.sendRecoveryEmail(email.getEmail(), token.getToken());
            return GeneralResponse.getResponse(HttpStatus.OK, "Email send success");
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

}
