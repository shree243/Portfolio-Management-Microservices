package com.users.authService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.users.authService.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}