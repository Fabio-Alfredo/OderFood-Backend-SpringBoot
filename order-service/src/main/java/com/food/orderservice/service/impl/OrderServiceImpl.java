package com.food.orderservice.service.impl;

import com.food.orderservice.domain.dto.CreateOrderDto;
import com.food.orderservice.domain.model.Order;
import com.food.orderservice.domain.model.OrderItem;
import com.food.orderservice.service.contract.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order createOrder(CreateOrderDto orderDto) {
        return null;
    }

    @Override
    public List<OrderItem> validateProducts(List<UUID> itemsIds) {
        return List.of();
    }
}
