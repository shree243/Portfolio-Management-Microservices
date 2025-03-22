package com.users.authService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.users.authService.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}