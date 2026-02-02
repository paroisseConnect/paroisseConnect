package com.example.worship_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.worship_service.dto.HoraireMesseRequest;
import com.example.worship_service.dto.HoraireMesseResourceType;
import com.example.worship_service.service.HoraireMesseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/messes")
@Tag(name = "Horaires Messes - Admin")
public class AdminMesseController {

	private final HoraireMesseService horaireService;

	public AdminMesseController(HoraireMesseService horaireService) {
		this.horaireService = horaireService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un horaire de messe")
	public Object create(@Valid @RequestBody HoraireMesseRequest request) {
		return horaireService.create(request);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Mettre a jour un horaire de messe")
	public Object update(@PathVariable Long id, @Valid @RequestBody HoraireMesseRequest request) {
		return horaireService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un horaire de messe")
	public void delete(@PathVariable Long id, @RequestParam HoraireMesseResourceType resourceType) {
		horaireService.delete(id, resourceType);
	}
}
