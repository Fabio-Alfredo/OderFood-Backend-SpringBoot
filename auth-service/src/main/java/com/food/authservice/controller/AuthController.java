package com.food.authservice.controller;

import com.food.authservice.domains.dtos.GeneralResponse;
import com.food.authservice.domains.dtos.token.TokenDto;
import com.food.authservice.domains.dtos.user.LoginDto;
import com.food.authservice.domains.dtos.user.RegisterDto;
import com.food.authservice.domains.models.Token;
import com.food.authservice.exceptions.HttpError;
import com.food.authservice.services.contract.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
}
