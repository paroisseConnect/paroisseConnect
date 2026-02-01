package com.example.worship_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.worship_service.entity.ProgrammeCelebrationEucharistique;

public interface ProgrammeCelebrationEucharistiqueRepository
		extends JpaRepository<ProgrammeCelebrationEucharistique, Long> {

	@EntityGraph(attributePaths = { "programme", "celebration", "celebration.uniteEcclesiale" })
	List<ProgrammeCelebrationEucharistique> findAll();

	@EntityGraph(attributePaths = { "programme", "celebration", "celebration.uniteEcclesiale" })
	Optional<ProgrammeCelebrationEucharistique> findById(Long id);
}
