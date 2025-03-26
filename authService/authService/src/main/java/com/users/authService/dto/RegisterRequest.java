package com.users.authService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@NotEmpty(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotEmpty(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	public RegisterRequest() {
	}

	public RegisterRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}