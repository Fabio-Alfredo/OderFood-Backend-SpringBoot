package com.food.orderservice.service.contract;

import com.food.orderservice.domain.dto.dishes.DishesQuantityDto;
import com.food.orderservice.domain.model.OrderItem;

import java.util.List;

public interface DishServiceClient {
    List<OrderItem>validateProducts(List<DishesQuantityDto> dishesDto);
}
