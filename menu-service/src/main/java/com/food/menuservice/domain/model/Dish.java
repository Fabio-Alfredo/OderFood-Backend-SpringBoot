package com.food.menuservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;
    private String description;
    @NotEmpty(message = "Category cannot be empty")
    private Double price;
    @Column(columnDefinition = "boolean default true")
    private Boolean available;
}
