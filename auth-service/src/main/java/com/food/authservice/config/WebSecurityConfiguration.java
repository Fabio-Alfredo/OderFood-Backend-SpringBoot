package com.food.authservice.config;

import com.food.authservice.domains.models.User;
import com.food.authservice.services.contract.UserService;
import com.food.authservice.utils.JwtFilterTools;
import com.food.authservice.utils.JwtTools;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final UserService userService;
    private final JwtFilterTools jwtFilterTools;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfiguration(UserService userService, JwtFilterTools jwtFilterTools, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtFilterTools = jwtFilterTools;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    AuthenticationManagerBuilder authenticationManagerBuilder (HttpSecurity httpSecurity)throws  Exception{
        AuthenticationManagerBuilder authentication = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authentication.userDetailsService(identifier ->{
            User user = userService.findByIdentifier(identifier);
            if(user == null){
                throw new UsernameNotFoundException("User not found with identifier: " + identifier);
            }
            return  user;
        })
                .passwordEncoder(passwordEncoder);
        return authentication;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(withDefaults()).csrf(csrf-> csrf.disable());

        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
        );

        httpSecurity.sessionManagement(management->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.exceptionHandling(handilg -> handilg.authenticationEntryPoint((req, res, ex)->{
            res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized: " + ex.getMessage()
            );
        }));
        httpSecurity.addFilter(jwtFilterTools);
        return httpSecurity.build();
    }
}
