package com.users.userService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.users.userService.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	Optional<UserProfile> findByEmail(String email);
}