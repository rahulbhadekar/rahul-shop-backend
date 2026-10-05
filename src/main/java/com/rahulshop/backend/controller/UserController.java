package com.rahulshop.backend.controller;

import com.rahulshop.backend.dto.LoginRequest;
import com.rahulshop.backend.dto.LoginResponse;
import com.rahulshop.backend.entity.User;
import com.rahulshop.backend.security.JwtService;
import com.rahulshop.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest request) {

        User user = userService.loginUser(request);

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        // JWT Token generate
        String token = jwtService.generateToken(user);

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}