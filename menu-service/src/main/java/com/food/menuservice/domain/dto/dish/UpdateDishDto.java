package com.food.menuservice.domain.dto.dish;

import lombok.Data;

@Data
public class UpdateDishDto {

    private String name;
    private String description;
    private Double price;
    private Boolean available = true;
}
