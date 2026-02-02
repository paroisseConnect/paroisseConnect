package com.example.activity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activity_service.entity.Activite;
import com.example.activity_service.entity.TypeActivite;
import com.example.activity_service.service.ActiviteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/activites")
@Tag(name = "Activites")
public class ActiviteController {

	private final ActiviteService service;

	public ActiviteController(ActiviteService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lister les activites")
	public List<Activite> list() {
		return service.listActivites();
	}

	@GetMapping("/{code}")
	@Operation(summary = "Detail d'une activite")
	public Activite get(@PathVariable Long code) {
		return service.getActivite(code);
	}

	@GetMapping("/types")
	@Operation(summary = "Lister les types d'activite")
	public List<TypeActivite> listTypes() {
		return service.listTypes();
	}
}
