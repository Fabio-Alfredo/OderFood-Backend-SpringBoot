package com.food.orderservice.controller;

import com.food.orderservice.Exceptions.HttpError;
import com.food.orderservice.domain.dto.order.CreateOrderDto;
import com.food.orderservice.service.contract.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateOrderDto orderDto) {
        try{
            orderService.createOrder(orderDto);
            return ResponseEntity.ok("Order created successfully");
        }catch (HttpError e){
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
}
