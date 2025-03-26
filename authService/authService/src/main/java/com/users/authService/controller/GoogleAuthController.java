package com.users.authService.controller;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.users.authService.constant.AuthProvider;
import com.users.authService.dto.AuthResponse;
import com.users.authService.entity.Role;
import com.users.authService.entity.User;
import com.users.authService.repository.RoleRepository;
import com.users.authService.repository.UserRepository;
import com.users.authService.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Google Authentication Controller",
description = "To login the user using google Social Login")
@RestController
@RequestMapping("/api/auth")
public class GoogleAuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private JwtService jwtService;

	  @Operation(summary = "Login the User using Google Social Login",
			  description = " @param request contains Token Id"
			      		+ "* @return JWT token if registration is successful")
	@PostMapping("/google-login")
	public ResponseEntity<AuthResponse> googleLogin(@RequestBody String idToken) throws Exception {
		FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
		String email = decodedToken.getEmail();

		User user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			Role userRole = roleRepository.findByName("USER");
			user = new User();
			user.setEmail(email);
			user.setProvider(AuthProvider.GOOGLE);
			user.setCreatedAt(LocalDateTime.now());
			user.setUpdatedAt(LocalDateTime.now());
			user.setRoles(Collections.singleton(userRole));
			userRepository.save(user);
		}

		String token = jwtService.generateToken(user);
		return ResponseEntity.ok(new AuthResponse(token));
	}
}
