package com.food.authservice.domains.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailDto {

    private String email;
}
