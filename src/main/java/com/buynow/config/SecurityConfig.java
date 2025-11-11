package com.buynow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ✅ JWT → no CSRF tokens required
                .csrf(AbstractHttpConfigurer::disable)
                // ✅ Custom JWT filter before default authorization
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)

                .authorizeHttpRequests(auth -> auth

                        /* ---------- PUBLIC USE-CASES ----------
                         * - Sign-up & login
                         * - Anyone can browse products or view a single product
                         */
                        .requestMatchers("/user/**","/actuator/**").permitAll()
                        .requestMatchers(
                                "/products",
                                "/products/{id}",
                                "/products/name/**",
                                "/products/download/**"

                        ).permitAll()

                        /* ---------- USER USE-CASES ----------
                         * - Add to cart, view cart, checkout
                         * - Only logged-in customers (ROLE_USER)
                         */
                        .requestMatchers(
                                "/cart/**",
                                "/orders/**"
                        ).hasRole("USER")

                        /* ---------- ADMIN USE-CASES ----------
                         * - Full product management
                         */
                        .requestMatchers(
                                "/products/add",
                                "/products/update/**",
                                "/products/delete/**"
                        ).hasRole("ADMIN")

                        /* ---------- FALLBACK ----------
                         * Everything else requires authentication
                         */
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
