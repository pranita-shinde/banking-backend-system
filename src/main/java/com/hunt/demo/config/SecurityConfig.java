package com.hunt.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hunt.demo.filter.JwtFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // ✅ Public APIs
                .requestMatchers("/auth/**").permitAll()

                // ✅ Allow new user creation
                .requestMatchers("/accounts").permitAll()

                // 👤 USER + ADMIN can access
                .requestMatchers("/accounts/**").hasAnyRole("USER", "ADMIN")

                // 👑 ADMIN only
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 🔐 Everything else secured
                .anyRequest().authenticated()
            )

            // ✅ ADD JWT FILTER HERE (VERY IMPORTANT)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}