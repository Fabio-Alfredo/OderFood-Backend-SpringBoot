package com.food.orderservice.service.impl;

import com.food.orderservice.Exceptions.HttpError;
import com.food.orderservice.domain.dto.dishes.DishesQuantityDto;
import com.food.orderservice.domain.dto.order.CreateOrderDto;
import com.food.orderservice.domain.dto.dishes.IdsDto;
import com.food.orderservice.domain.enums.StatusOrder;
import com.food.orderservice.domain.model.Order;
import com.food.orderservice.domain.model.OrderItem;
import com.food.orderservice.repositorie.OrderRepository;
import com.food.orderservice.service.contract.OrderService;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
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
    private final OrderRepository orderRepository;

    public OrderServiceImpl(DishServiceClientImpl dishServiceClient, KafkaTemplate<String, Order> kafkaTemplate, OrderRepository orderRepository) {
        this.dishServiceClient = dishServiceClient;
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = orderRepository;
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public Order createOrder(CreateOrderDto orderDto, UUID consumerId) {
        List<DishesQuantityDto> dishes = orderDto.getItemsIds();
        List<OrderItem> items = dishServiceClient.validateProducts(dishes);

        Order order = new Order();
        order.setItems(items);
        order.setTotal(orderDto.getTotal());
        order.setCustomerId(consumerId);

        Order newOrder = orderRepository.save(order);
        kafkaTemplate.send(orderCreatedTopic, order);

        return newOrder;
    }

    @Override
    public Order findById(UUID orderId) {
        try{
            Order order = orderRepository.findById(orderId).orElse(null);
            if(order == null) {
                throw new HttpError(HttpStatus.NOT_FOUND, "Order not found");
            }
            return order;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public List<Order> findByCustomerId(UUID userId) {
        try{
            List<Order> findAllOrders = orderRepository.findAllByCustomerId(userId);
            return findAllOrders != null ? findAllOrders : new ArrayList<>();
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public List<Order> findByStatusOrder(StatusOrder statusOrder) {
        try{
            List<Order> findAllOrders = orderRepository.findAllByStatus(statusOrder);
            return findAllOrders != null ? findAllOrders : new ArrayList<>();
        }catch (HttpError e){
            throw e;
        }
    }

}

