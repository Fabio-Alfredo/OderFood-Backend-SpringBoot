package com.food.authservice.domains.dtos.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordDto {

    @NotEmpty
    private String token;
    @NotEmpty
    private String newPassword;
}
