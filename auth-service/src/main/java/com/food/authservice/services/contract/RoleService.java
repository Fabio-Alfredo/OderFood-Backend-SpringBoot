package com.food.authservice.services.contract;

import com.food.authservice.domains.models.Role;

public interface RoleService {
    Role findById(String id);
}
