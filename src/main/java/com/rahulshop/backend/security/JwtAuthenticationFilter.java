package com.rahulshop.backend.security;

import com.rahulshop.backend.entity.User;
import com.rahulshop.backend.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserService userService
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Token nahi hai
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            // JWT se email nikalo
            String email = jwtService.extractEmail(token);

            // Database se user find karo
            User user = userService.findByEmail(email);

            if (user != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // USER / ADMIN role ko Spring Security authority me convert karo
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole()
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                Collections.singletonList(
                                        new SimpleGrantedAuthority("ROLE_" + user.getRole())
                                )
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Authentication Spring Security context me set karo
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT Token invalid or expired"
            );
        }

        filterChain.doFilter(request, response);
    }
}