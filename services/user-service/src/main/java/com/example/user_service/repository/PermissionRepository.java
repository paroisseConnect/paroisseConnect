package com.example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
