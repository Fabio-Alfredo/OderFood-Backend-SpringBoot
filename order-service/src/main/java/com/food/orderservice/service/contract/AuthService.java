package com.food.orderservice.service.contract;

import com.food.orderservice.domain.dto.auth.UserDto;

public interface AuthService {
    UserDto getUserAuthenticated();
}
