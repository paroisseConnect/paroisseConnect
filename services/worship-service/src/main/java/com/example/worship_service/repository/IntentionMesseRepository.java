package com.example.worship_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.worship_service.entity.IntentionMesse;

public interface IntentionMesseRepository extends JpaRepository<IntentionMesse, Long> {
}
