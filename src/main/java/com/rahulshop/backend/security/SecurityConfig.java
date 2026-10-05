package com.rahulshop.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF disable
                .csrf(csrf -> csrf.disable())

                // JWT based authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // OPTIONS request
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Login/Register public
                        .requestMatchers(
                                "/api/users/login",
                                "/api/users/register"
                        ).permitAll()

                        // Product GET public
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products/**"
                        ).permitAll()

                        // Product ADD only ADMIN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products/**"
                        ).hasRole("ADMIN")

                        // Product UPDATE only ADMIN
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/products/**"
                        ).hasRole("ADMIN")

                        // Product DELETE only ADMIN
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/products/**"
                        ).hasRole("ADMIN")

                        // Order status update only ADMIN
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/*/status"
                        ).hasRole("ADMIN")

                        // All remaining APIs require login
                        .anyRequest().authenticated()
                )

                // JWT Filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://127.0.0.1:5505",
                        "http://localhost:5505"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}