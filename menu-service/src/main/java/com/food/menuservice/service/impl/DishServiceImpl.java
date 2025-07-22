package com.food.menuservice.service.impl;

import com.food.menuservice.Exception.HttpError;
import com.food.menuservice.domain.dto.dish.CreateDishDto;
import com.food.menuservice.domain.dto.dish.IdsDishesDto;
import com.food.menuservice.domain.dto.dish.UpdateDishDto;
import com.food.menuservice.domain.model.Dish;
import com.food.menuservice.repository.DishRepository;
import com.food.menuservice.service.contract.DishService;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
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
        try{
            List<Dish>dishes =dishRepository.findAll();

            return  dishes;
        }catch (HttpError e){
            throw e;
        }
    }



    @Override
    public Dish findDishById(UUID id) {
        try{
            Dish dish = dishRepository.findById(id).orElse(null);
            if(dish == null)
                throw  new HttpError(HttpStatus.NOT_FOUND, "Dish not exist");
            return dish;
        }catch (HttpError e){
            throw  e;
        }
    }

    @Override
    public List<Dish> findAllByIds(IdsDishesDto idsDishesDto){
        try{
            List<Dish>dishes = dishRepository.findAllById(idsDishesDto.getIds());
            if(dishes.size() != idsDishesDto.getIds().size())
                throw  new HttpError(HttpStatus.NOT_FOUND, "Some dishes are not available");

            return dishes;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = {HttpError.class, Exception.class})
    public Dish updateDish(UpdateDishDto dishDto, UUID IdDish) {
        var dish = findDishById(IdDish);
        updateFields(dish, dishDto);
        return dishRepository.save(dish);
    }

    @Override
    public List<Dish> findAllDishesAvailable() {
        try{
            List<Dish> dishes = dishRepository.findAllByAvailable(true);
            if(dishes.isEmpty())
                throw new HttpError(HttpStatus.NOT_FOUND, "There are no dishes available");

            return dishes;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = {HttpError.class, Exception.class})
    public void deleteDish(UUID dish) {
        try{
            dishRepository.deleteById(dish);
        }catch (HttpError e){
            throw e;
        }
    }

    private void updateFields(Dish dish, UpdateDishDto dto) {
        if (dto.getName() != null) dish.setName(dto.getName());
        if (dto.getDescription() != null) dish.setDescription(dto.getDescription());
        if (dto.getPrice() != null) dish.setPrice(dto.getPrice());
        if (dto.getAvailable() != null) dish.setAvailable(dto.getAvailable());
    }

}
