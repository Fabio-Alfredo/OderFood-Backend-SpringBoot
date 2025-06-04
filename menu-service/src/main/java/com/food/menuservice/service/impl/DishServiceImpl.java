package com.food.menuservice.service.impl;

import com.food.menuservice.domain.dto.dish.CreateDishDto;
import com.food.menuservice.domain.model.Dish;
import com.food.menuservice.service.contract.DishService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DishServiceImpl implements DishService {
    @Override
    public Dish createDish(CreateDishDto dishDto) {
        return null;
    }

    @Override
    public List<Dish> findAllDish() {
        return List.of();
    }

    @Override
    public Dish findDishById(UUID id) {
        return null;
    }
}
