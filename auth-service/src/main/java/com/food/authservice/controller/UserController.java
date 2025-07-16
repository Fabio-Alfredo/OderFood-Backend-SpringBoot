package com.food.authservice.controller;

import com.food.authservice.domains.dtos.GeneralResponse;
import com.food.authservice.exceptions.HttpError;
import com.food.authservice.services.contract.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<GeneralResponse> getMe(){
        try {
            var user = userService.getAuthenticatedUser();
            return GeneralResponse.getResponse(HttpStatus.ACCEPTED, "User retrieved successfully", user);
        } catch (HttpError e) {
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }
}
