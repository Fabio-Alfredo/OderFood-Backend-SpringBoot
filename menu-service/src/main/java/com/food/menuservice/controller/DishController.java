package com.food.menuservice.controller;

import com.food.menuservice.Exception.HttpError;
import com.food.menuservice.domain.GeneralResponse;
import com.food.menuservice.domain.dto.dish.CreateDishDto;
import com.food.menuservice.domain.dto.dish.IdsDishesDto;
import com.food.menuservice.domain.model.Dish;
import com.food.menuservice.service.contract.DishService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse>createDish(@RequestBody @Valid CreateDishDto dishDto) {
        try{
            Dish dish = dishService.createDish(dishDto);
            return GeneralResponse.getResponse(HttpStatus.ACCEPTED,
                    "Dish created successfully",
                    dish);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<GeneralResponse>findAllDishes(){
        try{
            List<Dish>dishes = dishService.findAllDish();
            return GeneralResponse.getResponse(HttpStatus.ACCEPTED, dishes);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-ids")
    public ResponseEntity<GeneralResponse>findAllDishesByIds(@RequestBody @Valid IdsDishesDto idsDishesDto){
        try{
            List<Dish>dishes = dishService.findAllByIds(idsDishesDto);
            return GeneralResponse.getResponse(HttpStatus.ACCEPTED, dishes);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<GeneralResponse>findDishById(@PathVariable UUID id){
        try {
            Dish dish = dishService.findDishById(id);
            return GeneralResponse.getResponse(HttpStatus.ACCEPTED, dish);
        }catch (HttpError e){
            return  GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }

}
