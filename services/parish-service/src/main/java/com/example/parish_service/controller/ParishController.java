package com.example.parish_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parish_service.entity.UniteEcclesiale;
import com.example.parish_service.service.UniteEcclesialeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/parishes")
@Tag(name = "Parishes")
public class ParishController {

	private final UniteEcclesialeService service;

	public ParishController(UniteEcclesialeService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lister les paroisses")
	public List<UniteEcclesiale> list() {
		return service.listAll();
	}

	@GetMapping("/{code}")
	@Operation(summary = "Detail d'une paroisse")
	public UniteEcclesiale get(@PathVariable Long code) {
		return service.get(code);
	}
}
