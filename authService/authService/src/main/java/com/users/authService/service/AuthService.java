package com.users.authService.service;

import com.users.authService.dto.*;

public interface AuthService {
	AuthResponse register(RegisterRequest request);

	AuthResponse login(LoginRequest request);
}