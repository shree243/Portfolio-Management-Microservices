package com.users.userService.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.users.userService.dtos.UpdateProfileRequest;
import com.users.userService.dtos.UserProfileResponse;
import com.users.userService.entity.UserProfile;
import com.users.userService.repository.UserProfileRepository;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	@Autowired
	private UserProfileRepository repository;

	@Override
	public UserProfileResponse getProfile(String email) {
		UserProfile user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		UserProfileResponse response = new UserProfileResponse();
		response.setEmail(user.getEmail());
		response.setFullName(user.getFullName());
		response.setMobile(user.getMobile());
		response.setDob(user.getDob());
		response.setKycStatus(user.getKycStatus());
		return response;
	}

	@Override
	public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
		UserProfile user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		user.setFullName(request.getFullName());
		user.setMobile(request.getMobile());
		user.setDob(request.getDob());
		user.setKycStatus(request.getKycStatus());
		user.setUpdatedAt(LocalDateTime.now());
		repository.save(user);
		return getProfile(email);
	}
}