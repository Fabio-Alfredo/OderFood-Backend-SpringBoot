package com.food.menuservice.domain.dto.dish;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class IdsDishesDto {
    private List<UUID>ids;
}
