package com.food.menuservice.controller;

import com.food.menuservice.Exception.HttpError;
import com.food.menuservice.domain.GeneralResponse;
import com.food.menuservice.domain.dto.dish.CreateDishDto;
import com.food.menuservice.domain.model.Dish;
import com.food.menuservice.service.contract.DishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
