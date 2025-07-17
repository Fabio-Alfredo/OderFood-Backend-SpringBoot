package com.food.authservice.services.impl;

import com.food.authservice.domains.models.Role;
import com.food.authservice.exceptions.HttpError;
import com.food.authservice.repositorie.RoleRepository;
import com.food.authservice.services.contract.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(String id) {
        try{
            Role role = roleRepository.findById(id).orElse(null);
            if(role == null)
                throw new HttpError(HttpStatus.NOT_FOUND, "Invalid role not exist");

            return role;
        }catch (HttpError e){
            throw e;
        }
    }
}
