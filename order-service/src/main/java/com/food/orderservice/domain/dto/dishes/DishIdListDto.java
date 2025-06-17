package com.food.orderservice.domain.dto.dishes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishIdListDto {
    private List<UUID> ids;
}
