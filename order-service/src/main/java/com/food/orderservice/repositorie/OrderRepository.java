package com.food.orderservice.repositorie;

import com.food.orderservice.domain.enums.StatusOrder;
import com.food.orderservice.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order>findAllByCustomerId(UUID customerId);
    List<Order>findAllByStatus(StatusOrder statusOrder);
}
