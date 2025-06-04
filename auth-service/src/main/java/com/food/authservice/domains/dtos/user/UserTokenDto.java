package com.food.authservice.domains.dtos.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserTokenDto {
    private String email;
    private String username;
    private UUID id;
}
