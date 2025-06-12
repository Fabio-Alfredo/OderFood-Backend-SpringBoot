package com.food.orderservice.domain.dto.order;

import com.food.orderservice.domain.dto.dishes.DishesQuantityDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderDto {
    List<DishesQuantityDto> itemsIds;
    Double total;
}
