package com.food.paymentservice.services.contrat;


import com.food.paymentservice.domain.dtos.auth.UserDto;

public interface AuthService {
    UserDto getUserAuthenticated();
}
