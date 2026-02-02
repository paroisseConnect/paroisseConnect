package com.example.activity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_service.entity.TypeActivite;

public interface TypeActiviteRepository extends JpaRepository<TypeActivite, Long> {
}
