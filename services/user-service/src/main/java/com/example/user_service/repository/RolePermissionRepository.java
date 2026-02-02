package com.example.user_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
	List<RolePermission> findByCodeRole(Integer codeRole);

	void deleteByCodeRole(Integer codeRole);
}
