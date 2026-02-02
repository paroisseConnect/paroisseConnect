package com.example.user_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.entity.RoleUtilisateur;

public interface RoleUtilisateurRepository extends JpaRepository<RoleUtilisateur, Integer> {
	List<RoleUtilisateur> findByCodeUtilisateur(Integer codeUtilisateur);
}
