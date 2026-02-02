package com.example.activity_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.activity_service.dto.ActiviteRequest;
import com.example.activity_service.dto.TypeActiviteRequest;
import com.example.activity_service.entity.Activite;
import com.example.activity_service.entity.TypeActivite;
import com.example.activity_service.service.ActiviteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/activites")
@Tag(name = "Activites - Admin")
public class AdminActiviteController {

	private final ActiviteService service;

	public AdminActiviteController(ActiviteService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer une activite")
	public Activite create(@RequestBody ActiviteRequest request) {
		return service.createActivite(request);
	}

	@PutMapping("/{code}")
	@Operation(summary = "Mettre a jour une activite")
	public Activite update(@PathVariable Long code, @RequestBody ActiviteRequest request) {
		return service.updateActivite(code, request);
	}

	@DeleteMapping("/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer une activite")
	public void delete(@PathVariable Long code) {
		service.deleteActivite(code);
	}

	@PostMapping("/types")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un type d'activite")
	public TypeActivite createType(@RequestBody TypeActiviteRequest request) {
		return service.createType(request);
	}

	@PutMapping("/types/{code}")
	@Operation(summary = "Mettre a jour un type d'activite")
	public TypeActivite updateType(@PathVariable Long code, @RequestBody TypeActiviteRequest request) {
		return service.updateType(code, request);
	}

	@DeleteMapping("/types/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un type d'activite")
	public void deleteType(@PathVariable Long code) {
		service.deleteType(code);
	}
}
