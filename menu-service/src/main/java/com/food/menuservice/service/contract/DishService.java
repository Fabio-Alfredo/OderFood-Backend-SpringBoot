package com.food.menuservice.service.contract;

import com.food.menuservice.domain.dto.dish.CreateDishDto;
import com.food.menuservice.domain.dto.dish.IdsDishesDto;
import com.food.menuservice.domain.model.Dish;

import java.util.List;
import java.util.UUID;

public interface DishService {
    Dish createDish(CreateDishDto dishDto);
    List<Dish>findAllDish();
    Dish findDishById(UUID id);
    List<Dish>findAllByIds(IdsDishesDto idsDishesDto);
}
