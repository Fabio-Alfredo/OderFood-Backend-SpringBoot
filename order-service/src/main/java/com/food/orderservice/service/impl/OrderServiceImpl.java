package com.food.orderservice.service.impl;

import com.food.orderservice.Exceptions.HttpError;
import com.food.orderservice.domain.OrderEvent;
import com.food.orderservice.domain.dto.dishes.DishQuantityRequestDto;
import com.food.orderservice.domain.dto.order.CreateOrderDto;
import com.food.orderservice.domain.dto.order.UpdateStatusOrder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    private final DishServiceClientImpl dishServiceClient;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(DishServiceClientImpl dishServiceClient, KafkaTemplate<String, OrderEvent> kafkaTemplate, OrderRepository orderRepository) {
        this.dishServiceClient = dishServiceClient;
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = orderRepository;
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public Order createOrder(CreateOrderDto orderDto, UUID consumerId) {
        List<DishQuantityRequestDto> dishes = orderDto.getItemsIds();
        List<OrderItem> items = dishServiceClient.validateProducts(dishes);

        Order order = new Order();
        order.setItems(items);
        order.setTotal(calculateTotalPrice(items));
        order.setCustomerId(consumerId);

        Order newOrder = orderRepository.save(order);
        OrderEvent<Order> orderEvent = new OrderEvent<>("create-order", newOrder);

        kafkaTemplate.send(orderCreatedTopic, orderEvent);

        return newOrder;
    }

    private Double calculateTotalPrice(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
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

    @Override
    public Order updateStatusOrder(UpdateStatusOrder statusOrder) {
        try{
            System.out.println("Updating order status: " + statusOrder);
            Order order = orderRepository.findById(statusOrder.getOrderId()).orElse(null);
            if(order == null) {
                throw new HttpError(HttpStatus.NOT_FOUND, "Order not found");
            }

            if(statusOrder.getStatus() == StatusOrder.CANCELLED && order.getStatus() == StatusOrder.DELIVERED) {
                throw new HttpError(HttpStatus.BAD_REQUEST, "Cannot cancel a delivered order.");
            }

            order.setStatus(statusOrder.getStatus());
            Order updatedOrder = orderRepository.save(order);

            if(statusOrder.getStatus() == StatusOrder.CANCELLED){
                OrderEvent<Order> orderEvent = new OrderEvent<>("cancel-order", updatedOrder);
                kafkaTemplate.send(orderCreatedTopic, orderEvent);
            }
            return updatedOrder;
        }catch (HttpError e) {
            throw e;
        }
    }

}

