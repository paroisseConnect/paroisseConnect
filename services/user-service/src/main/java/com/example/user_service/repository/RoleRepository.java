package com.example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
