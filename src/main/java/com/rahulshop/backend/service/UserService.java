package com.rahulshop.backend.service;

import com.rahulshop.backend.entity.User;
import com.rahulshop.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.rahulshop.backend.dto.LoginRequest;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setRole("USER");

        return userRepository.save(user);
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElse(null);
    }
    public User loginUser(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return null;
        }

        boolean passwordMatch =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatch) {
            return null;
        }

        return user;
    }
}