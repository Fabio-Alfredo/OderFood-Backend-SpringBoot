package com.food.orderservice.service.impl;

import com.food.orderservice.Exceptions.HttpError;
import com.food.orderservice.domain.dto.dishes.DishesQuantityDto;
import com.food.orderservice.domain.dto.order.CreateOrderDto;
import com.food.orderservice.domain.dto.dishes.IdsDto;
import com.food.orderservice.domain.model.Order;
import com.food.orderservice.domain.model.OrderItem;
import com.food.orderservice.service.contract.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    private final DishServiceClientImpl dishServiceClient;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    public OrderServiceImpl(DishServiceClientImpl dishServiceClient, KafkaTemplate<String, Order> kafkaTemplate) {
        this.dishServiceClient = dishServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public Order createOrder(CreateOrderDto orderDto) {
        List<DishesQuantityDto> dishes = orderDto.getItemsIds();
        List<OrderItem> items = dishServiceClient.validateProducts(dishes);

        Order order = new Order();
        order.setItems(items);
        order.setTotal(orderDto.getTotal());
        kafkaTemplate.send(orderCreatedTopic, order);
        System.out.println("Order created and sent to Kafka: " + order.getItems() + " Total: " + order.getTotal());
        return null;
    }

}

