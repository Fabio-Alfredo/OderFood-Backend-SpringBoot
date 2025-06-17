package com.food.orderservice.service.contract;

import com.food.orderservice.domain.dto.dishes.DishQuantityRequestDto;
import com.food.orderservice.domain.model.OrderItem;

import java.util.List;

public interface DishServiceClient {
    List<OrderItem>validateProducts(List<DishQuantityRequestDto> dishesDto);
}
