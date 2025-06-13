package com.food.orderservice.domain.dto.order;

import com.food.orderservice.domain.dto.dishes.DishesQuantityDto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderDto {
    @NotEmpty(message = "Items cannot be empty")
    List<DishesQuantityDto> itemsIds;

    @Digits(integer = 10, fraction = 2, message = "Total must be a valid number")
            @PositiveOrZero(message = "Total must be positive")
            @NotNull(message = "Total cannot be null")
    Double total;
}
