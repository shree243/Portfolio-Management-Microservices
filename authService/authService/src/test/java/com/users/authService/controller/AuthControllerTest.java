package com.users.authService.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.authService.dto.AuthResponse;
import com.users.authService.dto.LoginRequest;
import com.users.authService.dto.RegisterRequest;
import com.users.authService.exception.UserAlreadyExistsException;
import com.users.authService.service.AuthServiceImpl;

@SpringBootTest
public class AuthControllerTest {

	@Mock
	private AuthServiceImpl authService;

	@Autowired
	AuthController auth;

	@Autowired
	private ObjectMapper objectMapper;

	private RegisterRequest request;
	private AuthResponse response;

	@BeforeEach
	public void setup() {
		request = new RegisterRequest("test89@example.com", "password123");
		response = new AuthResponse("dummy-token");
	}

//	to test the register controller method
	@Test
	public void register_ShouldReturnOk() throws Exception {
		when(authService.register(any(RegisterRequest.class))).thenReturn(response);
		ResponseEntity<AuthResponse> ans = auth.register(request);
		assertEquals(ans.getStatusCode(), org.springframework.http.HttpStatus.OK);
	}

//	to test the login controller method
	@Test
	public void login_ShouldReturnOk() throws Exception {
		LoginRequest loginRequest = new LoginRequest("test89@example.com", "password123");
		AuthResponse expectedResponse = new AuthResponse("dummy-login-token");
		when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);
		ResponseEntity<AuthResponse> responseEntity = auth.login(loginRequest);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

//	to test the register controller method should throw exception user already exist
	@Test
	public void register_ShouldThrowUserAlreadyExistsException() {
		when(authService.register(any(RegisterRequest.class)))
				.thenThrow(new UserAlreadyExistsException("Email is already in use"));
		assertThrows(UserAlreadyExistsException.class, () -> auth.register(request));
	}
}
