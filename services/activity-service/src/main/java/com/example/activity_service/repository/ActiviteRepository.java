package com.example.activity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_service.entity.Activite;

public interface ActiviteRepository extends JpaRepository<Activite, Long> {
}
