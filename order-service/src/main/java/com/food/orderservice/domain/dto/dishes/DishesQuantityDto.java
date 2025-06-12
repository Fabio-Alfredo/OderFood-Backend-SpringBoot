package com.food.orderservice.domain.dto.dishes;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DishesQuantityDto {
    private UUID dishId;
    private Integer quantity;

}
