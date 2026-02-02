package com.example.parish_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.parish_service.dto.UniteEcclesialeRequest;
import com.example.parish_service.entity.UniteEcclesiale;
import com.example.parish_service.service.UniteEcclesialeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Parishes - Admin")
public class AdminParishController {

	private final UniteEcclesialeService service;

	public AdminParishController(UniteEcclesialeService service) {
		this.service = service;
	}

	@PostMapping("/parishes")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer une paroisse")
	public UniteEcclesiale createParish(@RequestBody UniteEcclesialeRequest request) {
		return service.create(request);
	}

	@PutMapping("/parishes/{code}")
	@Operation(summary = "Mettre a jour une paroisse")
	public UniteEcclesiale updateParish(@PathVariable Long code, @RequestBody UniteEcclesialeRequest request) {
		return service.update(code, request);
	}

	@DeleteMapping("/parishes/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer une paroisse")
	public void deleteParish(@PathVariable Long code) {
		service.delete(code);
	}

	@PostMapping("/unites-ecclesiales")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer une unite ecclesiale")
	public UniteEcclesiale createUnite(@RequestBody UniteEcclesialeRequest request) {
		return service.create(request);
	}

	@PutMapping("/unites-ecclesiales/{code}")
	@Operation(summary = "Mettre a jour une unite ecclesiale")
	public UniteEcclesiale updateUnite(@PathVariable Long code, @RequestBody UniteEcclesialeRequest request) {
		return service.update(code, request);
	}

	@DeleteMapping("/unites-ecclesiales/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer une unite ecclesiale")
	public void deleteUnite(@PathVariable Long code) {
		service.delete(code);
	}
}
