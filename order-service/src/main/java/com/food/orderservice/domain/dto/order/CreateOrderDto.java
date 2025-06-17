package com.food.orderservice.domain.dto.order;

import com.food.orderservice.domain.dto.dishes.DishQuantityRequestDto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    @NotEmpty(message = "Items cannot be empty")
    List<DishQuantityRequestDto> itemsIds;
}
