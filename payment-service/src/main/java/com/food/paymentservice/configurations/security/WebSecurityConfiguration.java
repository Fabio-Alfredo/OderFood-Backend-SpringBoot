package com.food.paymentservice.configurations.security;

import com.food.paymentservice.utils.JwtFilterTools;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final JwtFilterTools jwtFilterTools;

    public WebSecurityConfiguration(JwtFilterTools jwtFilterTools) {
        this.jwtFilterTools = jwtFilterTools;
    }

    @Bean
    public SecurityFilterChain securityFilterChai(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.httpBasic(withDefaults()).csrf(csrf-> csrf.disable());

        httpSecurity.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/api/payments/algo").permitAll()
                        .anyRequest().authenticated()
        );
        httpSecurity.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.exceptionHandling(exception -> exception.authenticationEntryPoint((req, res, ex)->{
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request");
        }));

        httpSecurity.addFilterBefore(jwtFilterTools, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
