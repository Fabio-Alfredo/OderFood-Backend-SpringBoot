package com.food.authservice.repositorie;

import com.food.authservice.domains.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmailOrUsername(String email, String username);
    User findByEmail(String email);
}
