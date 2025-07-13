package com.food.orderservice.service.impl;

import com.food.orderservice.Exceptions.HttpError;
import com.food.orderservice.domain.dto.common.ApisResponse;
import com.food.orderservice.domain.dto.dishes.DishDto;
import com.food.orderservice.domain.dto.dishes.DishQuantityRequestDto;
import com.food.orderservice.domain.dto.dishes.DishIdListDto;
import com.food.orderservice.domain.model.OrderItem;
import com.food.orderservice.service.contract.DishServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DishServiceClientImpl implements DishServiceClient {

    @Value("${dishes.url}")
    private String dishesUrl;

    private final RestTemplate restTemplate;

    public DishServiceClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<OrderItem> validateProducts(List<DishQuantityRequestDto> dishesDto) {
        try{
            List<UUID>idsItems = dishesDto.stream().map(DishQuantityRequestDto::getDishId).toList();
            List<DishDto> dishes = fetchDishesByIds(idsItems);

            List<OrderItem> items = getOrderItems(dishesDto, dishes);
            return  items;
        }catch (RestClientResponseException e){
            throw new HttpError((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    private static List<OrderItem> getOrderItems(List<DishQuantityRequestDto> dishesDto, List<DishDto> dishes) {
        List<OrderItem> items = new ArrayList<>();

        for (int i = 0; i < dishes.size(); i++) {
            DishDto dish = dishes.get(i);
            DishQuantityRequestDto quantityDto = dishesDto.get(i);

            OrderItem item = new OrderItem();
            item.setDishId(dish.getId());
            item.setName(dish.getName());
            item.setPrice(dish.getPrice());
            item.setQuantity(quantityDto.getQuantity());

            items.add(item);
        }
        return items;
    }

    private List<DishDto> fetchDishesByIds(List<UUID> itemsIds) {
        try {
            // Construir la URL con query params
            StringBuilder urlBuilder = new StringBuilder(dishesUrl + "/find-by-ids?");
            for (UUID id : itemsIds) {
                urlBuilder.append("Ids=").append(id.toString()).append("&");
            }
            // Eliminar el último '&'
            String url = urlBuilder.substring(0, urlBuilder.length() - 1);

            ParameterizedTypeReference<ApisResponse<List<DishDto>>> responseType =
                    new ParameterizedTypeReference<>() {};

            ResponseEntity<ApisResponse<List<DishDto>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null, // no hay body en GET
                    responseType
            );

            System.out.println("Response from Dish Service: " + response.getBody());

            return response.getBody().getData();
        } catch (RestClientResponseException e) {
            throw new HttpError((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }



}
