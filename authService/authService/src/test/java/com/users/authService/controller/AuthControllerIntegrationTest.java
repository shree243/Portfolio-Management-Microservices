package com.users.authService.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.authService.dto.AuthResponse;
import com.users.authService.dto.RegisterRequest;
import com.users.authService.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthService authService;

	@Autowired
	private ObjectMapper objectMapper;

	private RegisterRequest registerRequest;
	private AuthResponse authResponse;

	@BeforeEach
	public void setup() {
		registerRequest = new RegisterRequest("test123@example.com", "password123");
		authResponse = new AuthResponse("dummy-jwt-token");
	}

//	Integration test for registration
	@Test
	public void registerUser_ShouldReturnJwtToken() throws Exception {
		System.out.println(objectMapper.writeValueAsString(registerRequest));
		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("dummy-jwt-token"));
	}
}
