package com.example.worship_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.worship_service.entity.CelebrationEucharistique;

public interface CelebrationEucharistiqueRepository extends JpaRepository<CelebrationEucharistique, Long> {

	@Query("select distinct c.codeType from CelebrationEucharistique c where c.codeType is not null")
	List<String> findDistinctCodeTypes();
}
