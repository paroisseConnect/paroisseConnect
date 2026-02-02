package com.example.worship_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.worship_service.entity.Confession;

public interface ConfessionRepository extends JpaRepository<Confession, Long> {
}
