package com.food.authservice.domains.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UserTokenDto {
    private String email;
    private String username;
    private UUID id;
}
