package com.food.orderservice.domain.model;

import com.food.orderservice.domain.enums.StatusOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID customerId;
    @ElementCollection
            @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    List<OrderItem> items;
    private Double total;
    private  LocalDateTime timestamp = LocalDateTime.now();
    private StatusOrder status = StatusOrder.CREATED;
}
