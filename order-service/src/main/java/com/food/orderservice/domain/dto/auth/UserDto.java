package com.food.orderservice.domain.dto.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
        private String username;
        private String email;
        private UUID id;
}
