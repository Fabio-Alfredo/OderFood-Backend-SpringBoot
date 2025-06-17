package com.food.orderservice.domain.dto.dishes;

import lombok.Data;

import java.util.UUID;

@Data
public class DishQuantityRequestDto {
    private UUID dishId;
    private Integer quantity;

}
