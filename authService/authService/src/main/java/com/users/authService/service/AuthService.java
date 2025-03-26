package com.users.authService.service;

import org.springframework.stereotype.Service;

import com.users.authService.dto.*;

@Service
public interface AuthService {
	AuthResponse register(RegisterRequest request);

	AuthResponse login(LoginRequest request);
}