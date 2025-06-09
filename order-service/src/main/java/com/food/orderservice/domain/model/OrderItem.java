package com.food.orderservice.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.UUID;

@Embeddable
@Data
public class OrderItem {

    private UUID dishId;
    private String name;
    private Integer quantity;
    private Double price;
}
