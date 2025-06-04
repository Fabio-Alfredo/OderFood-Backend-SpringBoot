package com.food.menuservice.domain.dto.dish;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateDishDto {
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    @NotEmpty(message = "Category cannot be empty")
    private Double price;
}
