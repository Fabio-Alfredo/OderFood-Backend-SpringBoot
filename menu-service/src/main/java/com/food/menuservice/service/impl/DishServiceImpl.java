package com.food.menuservice.service.impl;

import com.food.menuservice.Exception.HttpError;
import com.food.menuservice.domain.dto.dish.CreateDishDto;
import com.food.menuservice.domain.model.Dish;
import com.food.menuservice.repository.DishRepository;
import com.food.menuservice.service.contract.DishService;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    @Transactional(rollbackOn = {HttpError.class, Exception.class})
    public Dish createDish(CreateDishDto dishDto) {
        try{
            Dish dish = dishRepository.findByName(dishDto.getName());
            if(dish != null){
                throw new HttpError(HttpStatus.BAD_REQUEST, "Dish with this name already exists");
            }
            Dish newDish = new Dish();
            newDish.setName(dishDto.getName());
            newDish.setDescription(dishDto.getDescription());
            newDish.setPrice(dishDto.getPrice());

            return dishRepository.save(newDish);
        }catch (HttpError e){
            throw e;
        }
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
