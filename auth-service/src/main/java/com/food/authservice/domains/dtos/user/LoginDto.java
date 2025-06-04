package com.food.authservice.domains.dtos.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {

    @NotEmpty(message = "Identifier cannot be empty")
    private String identifier;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
