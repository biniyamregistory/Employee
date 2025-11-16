package com.commerce.user_service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public String register(User user) {
        if (userRepository.existsById(user.getUsername())) {
            return "Username already exists!";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    public String login(String username, String password) {
        return userRepository.findById(username)
                .map(user -> encoder.matches(password, user.getPassword())
                        ? jwtUtil.generateToken(username)
                        : "Invalid password!"
                )
                .orElse("User not found!");
    }
}

