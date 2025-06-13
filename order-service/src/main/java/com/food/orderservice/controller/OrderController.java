package com.food.orderservice.controller;

import com.food.orderservice.Exceptions.HttpError;
import com.food.orderservice.domain.dto.auth.UserDto;
import com.food.orderservice.domain.dto.common.GeneralResponse;
import com.food.orderservice.domain.dto.order.CreateOrderDto;
import com.food.orderservice.domain.model.Order;
import com.food.orderservice.service.contract.AuthService;
import com.food.orderservice.service.contract.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping("/create")
    public ResponseEntity<GeneralResponse> createOrder(@RequestBody @Valid CreateOrderDto orderDto) {
        try{
            UserDto user = authService.getUserAuthenticated();
            Order order = orderService.createOrder(orderDto, user.getId());
            return GeneralResponse.getResponse(
                    HttpStatus.CREATED,
                "Order created successfully", order);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }
}
