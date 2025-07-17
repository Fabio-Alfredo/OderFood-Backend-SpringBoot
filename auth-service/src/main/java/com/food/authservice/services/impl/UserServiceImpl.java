package com.food.authservice.services.impl;

import com.food.authservice.domains.dtos.user.LoginDto;
import com.food.authservice.domains.dtos.user.RegisterDto;
import com.food.authservice.domains.dtos.user.UserTokenDto;
import com.food.authservice.domains.enums.TokenType;
import com.food.authservice.domains.models.Role;
import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;
import com.food.authservice.exceptions.HttpError;
import com.food.authservice.repositorie.TokenRepository;
import com.food.authservice.repositorie.UserRepository;
import com.food.authservice.services.contract.RoleService;
import com.food.authservice.services.contract.UserService;
import com.food.authservice.utils.JwtTools;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTools jwtTools;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, JwtTools jwtTools, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, RoleService roleService) {
        this.userRepository = userRepository;
        this.jwtTools = jwtTools;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    @Override
    public User findByIdentifier(String identifier) {
        try{
            User user = userRepository.findByEmailOrUsername(identifier, identifier);
            if (user == null) {
                throw new HttpError(HttpStatus.NOT_FOUND, "User not found with identifier: " + identifier);
            }
            return user;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new HttpError(HttpStatus.NOT_FOUND, "User not found with email: " + email);
            }
            return user;
        } catch (HttpError e) {
            throw e;
        }
    }

    @Override
    public void registerUser(RegisterDto registerDto) {
        try{
            User user = userRepository.findByEmailOrUsername(registerDto.getEmail(), registerDto.getUsername());
            if(user != null){
                throw new HttpError(HttpStatus.BAD_REQUEST, "User already exists with email or username: " + registerDto.getEmail() + ", " + registerDto.getUsername());
            }

            Role role = roleService.findById("ADMIN");

            user = modelMapper.map(registerDto, User.class);
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setRoles(List.of(role));

            System.out.println(user);
            userRepository.save(user);
        }catch (HttpError e) {
            throw e;
        }
    }

    @Override
    public Token loginUser(LoginDto loginDto) {
        try{
            User user = userRepository.findByEmailOrUsername(loginDto.getIdentifier(), loginDto.getIdentifier());
            if(user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                throw new HttpError(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
            Token token = registerToken(user);

            return token;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public List<User> findAllUsers() {
        try{
            List<User> users = userRepository.findAll();
            if(users.isEmpty())
                throw new HttpError(HttpStatus.NOT_FOUND, "NOt users in services");

            return users;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public Token registerToken(User user) {
        try{
            cleanTokens(user);
            //generar token
            String tokenString = jwtTools.generateToken(user);

            Token token = new Token(user, tokenString);

            return tokenRepository.save(token);
        }catch (HttpError e) {
            throw e;
        }
    }

    @Override
    public void cleanTokens(User user) {
        try{
            List<Token> tokens = tokenRepository.findByUserAndIsValid(user, true);
            tokens.forEach(t ->{
                if(!jwtTools.verifyToken(t.getToken())) {
                    t.setIsValid(false);
                    tokenRepository.save(t);
                }
            });
        }catch (HttpError e) {
            throw e;
        }
    }

    @Override
    public User getAuthenticatedUser() {
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(user == null) {
                throw new HttpError(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }

            return user;
        }catch (HttpError e) {
            throw e;
        }
    }
}
