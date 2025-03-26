package com.users.authService.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.users.authService.constant.AuthProvider;
import com.users.authService.dto.AuthResponse;
import com.users.authService.dto.LoginRequest;
import com.users.authService.dto.RegisterRequest;
import com.users.authService.entity.Role;
import com.users.authService.entity.User;
import com.users.authService.exception.InvalidCredentialsException;
import com.users.authService.exception.UserAlreadyExistsException;
import com.users.authService.exception.UserNotFoundException;
import com.users.authService.repository.RoleRepository;
import com.users.authService.repository.UserRepository;
import com.users.authService.service.AuthServiceImpl;
import com.users.authService.service.JwtService;

@SpringBootTest
public class AuthServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@InjectMocks
	private AuthServiceImpl authService;

	private RegisterRequest registerRequest;
	private LoginRequest loginRequest;
	private User user;
	private Role role;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		registerRequest = new RegisterRequest("test@example.com", "password123");
		loginRequest = new LoginRequest("test@example.com", "password123");

		role = new Role();
		role.setName("USER");

		user = new User();
		user.setEmail("test@example.com");
		user.setPassword("encoded-password");
		user.setProvider(AuthProvider.LOCAL);
		user.setRoles(Set.of(role));
	}

//	@Test
	void register_ShouldReturnAuthToken() {
		when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
		when(roleRepository.findByName("USER")).thenReturn(role);
		when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encoded-password");
		when(jwtService.generateToken(any(User.class))).thenReturn("mock-token");

		AuthResponse response = authService.register(registerRequest);

		assertNotNull(response);
		assertEquals("mock-token", response.getToken());
	}

	// Register: User already exists
//	@Test
	void register_ShouldThrowUserAlreadyExistsException() {
		when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));

		assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerRequest));
		verify(userRepository, never()).save(any(User.class));
	}

	// Login: Success
//	@Test
	void login_ShouldReturnAuthToken() {
		when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
		when(jwtService.generateToken(user)).thenReturn("login-token");

		AuthResponse response = authService.login(loginRequest);

		assertNotNull(response);
		assertEquals("login-token", response.getToken());
	}

	// Login: User not found
//	@Test
	void login_ShouldThrowUserNotFoundException() {
		when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> authService.login(loginRequest));
	}

	// Login: Invalid credentials
//	@Test
	void login_ShouldThrowInvalidCredentialsException() {
		when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

		assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
	}

}
