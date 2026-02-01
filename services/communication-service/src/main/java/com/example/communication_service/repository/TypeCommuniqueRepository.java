package com.example.communication_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.communication_service.entity.TypeCommunique;

public interface TypeCommuniqueRepository extends JpaRepository<TypeCommunique, Long> {
}
