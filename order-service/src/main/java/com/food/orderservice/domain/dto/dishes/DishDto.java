package com.food.orderservice.domain.dto.dishes;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.UUID;

@Data
public class DishDto {
    private UUID dishId;
    private String name;
    private Double price;
}
