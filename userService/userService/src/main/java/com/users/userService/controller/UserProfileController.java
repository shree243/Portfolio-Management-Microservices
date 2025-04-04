package com.users.userService.controller;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.userService.dtos.UpdateProfileRequest;
import com.users.userService.dtos.UserProfileResponse;
import com.users.userService.service.UserProfileService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Controller",
description = "to update the user and get the user details")
@RestController
@RequestMapping("/api/user")
public class UserProfileController {

	@Autowired
	private UserProfileService service;

	private final String jwtSecret = "";

	private String extractEmailFromToken(String token) {
		Key key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	@Operation(summary = "To get User profile",
		      description = " @param request contains token release by auth service"
		      		+ "* @return user details if user available in database")
	@GetMapping("/profile")
	public ResponseEntity<UserProfileResponse> getProfile(@RequestHeader("Authorization") String token) {
		String email = extractEmailFromToken(token.replace("Bearer ", ""));
		return ResponseEntity.ok(service.getProfile(email));
	}

	@Operation(summary = "To update User profile",
		      description = " @param request contains token release by auth service and user updated data"
		      		+ "* @return user updated details and email")
	@PutMapping("/profile")
	public ResponseEntity<UserProfileResponse> updateProfile(@RequestHeader("Authorization") String token,
			@RequestBody UpdateProfileRequest request) {
		String email = extractEmailFromToken(token.replace("Bearer ", ""));
		return ResponseEntity.ok(service.updateProfile(email, request));
	}
}