package com.food.orderservice.service.contract;


import com.food.orderservice.domain.dto.order.CreateOrderDto;
import com.food.orderservice.domain.model.Order;
import com.food.orderservice.domain.model.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder(CreateOrderDto orderDto);
//    List<OrderItem>validateProducts(List<UUID> itemsIds);
}
