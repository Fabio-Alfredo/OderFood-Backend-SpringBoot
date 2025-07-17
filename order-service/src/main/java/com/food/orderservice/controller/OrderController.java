package com.food.orderservice.controller;

import com.food.orderservice.Exceptions.HttpError;
import com.food.orderservice.domain.dto.auth.UserDto;
import com.food.orderservice.domain.dto.common.GeneralResponse;
import com.food.orderservice.domain.dto.order.CreateOrderDto;
import com.food.orderservice.domain.dto.order.UpdateStatusOrder;
import com.food.orderservice.domain.enums.StatusOrder;
import com.food.orderservice.domain.model.Order;
import com.food.orderservice.service.contract.AuthService;
import com.food.orderservice.service.contract.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @PostMapping("/create")
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

    @PutMapping("/update-status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse>updateStatus(@RequestBody @Valid UpdateStatusOrder statusOrder){
        try{
            UserDto user = authService.getUserAuthenticated();
            Order order = orderService.updateStatusOrder(statusOrder);
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Order status updated successfully",
                    order
            );
        }catch (HttpError e) {
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-customer")
    public ResponseEntity<GeneralResponse>findAllByCustomer(){
        try{
            UserDto user = authService.getUserAuthenticated();
            List<Order> orders = orderService.findByCustomerId(user.getId());
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Orders found successfully",
                    orders
                    );
        }catch (HttpError e) {
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-id/{orderId}")
    public ResponseEntity<GeneralResponse>findById(@PathVariable UUID orderId) {
        try{
            Order order = orderService.findById(orderId);
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Order found successfully",
                    order
            );
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-status/{statusOrder}")
    public ResponseEntity<GeneralResponse> findByStatusOrder(@PathVariable String statusOrder) {
        try{
            StatusOrder status = StatusOrder.valueOf(statusOrder.toUpperCase());
            List<Order> orders = orderService.findByStatusOrder(status);
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Orders found successfully",
                    orders
            );
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }
}
