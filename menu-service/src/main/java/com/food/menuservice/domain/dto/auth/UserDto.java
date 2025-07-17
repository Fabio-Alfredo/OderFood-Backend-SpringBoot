package com.food.menuservice.domain.dto.auth;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private String email;
    private UUID id;
    private List<String>roles;

    public List<GrantedAuthority>getAuthorities(){
        List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }
}
