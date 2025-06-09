package com.food.orderservice.domain.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderDto {
    List<UUID> itemsIds;
    Double total;
}
