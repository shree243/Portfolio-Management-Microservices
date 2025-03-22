package com.users.userService.service;

import com.users.userService.dtos.UpdateProfileRequest;
import com.users.userService.dtos.UserProfileResponse;

public interface UserProfileService {
	UserProfileResponse getProfile(String email);

	UserProfileResponse updateProfile(String email, UpdateProfileRequest request);
}