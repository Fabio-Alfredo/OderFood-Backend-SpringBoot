package com.food.orderservice.service.contract;


import com.food.orderservice.domain.dto.order.CreateOrderDto;
import com.food.orderservice.domain.enums.StatusOrder;
import com.food.orderservice.domain.model.Order;
import com.food.orderservice.domain.model.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order creatOrder(CreateOrderDto orderDto, UUID consumerId);
    Order findById(UUID orderId);
    List<Order>findByCustomerId(UUID userId);
    List<Order>findByStatusOrder(StatusOrder statusOrder);
}
