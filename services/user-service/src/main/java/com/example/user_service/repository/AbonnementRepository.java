package com.example.user_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.entity.AbonnementUniteEcclesiale;

public interface AbonnementRepository extends JpaRepository<AbonnementUniteEcclesiale, Integer> {
	List<AbonnementUniteEcclesiale> findByCodeUtilisateur(Integer codeUtilisateur);
}
