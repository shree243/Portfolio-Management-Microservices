package com.users.authService.service;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.users.authService.constant.AuthProvider;
import com.users.authService.dto.AuthResponse;
import com.users.authService.dto.LoginRequest;
import com.users.authService.dto.RegisterRequest;
import com.users.authService.entity.Role;
import com.users.authService.entity.User;
import com.users.authService.repository.RoleRepository;
import com.users.authService.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("USER");
        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .provider(AuthProvider.LOCAL)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .roles(Set.of(userRole))
            .build();
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user));


    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return new AuthResponse(jwtService.generateToken(user));


    }
}