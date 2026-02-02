package com.example.content_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.content_service.dto.LieuSaintRequest;
import com.example.content_service.dto.ObjetSacreRequest;
import com.example.content_service.entity.LieuSaint;
import com.example.content_service.entity.ObjetSacre;
import com.example.content_service.service.ContentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Contenus spirituels - Admin")
public class AdminContentController {

	private final ContentService service;

	public AdminContentController(ContentService service) {
		this.service = service;
	}

	@PostMapping("/lieux-saints")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un lieu spirituel")
	public LieuSaint createLieu(@RequestBody LieuSaintRequest request) {
		return service.createLieu(request);
	}

	@PutMapping("/lieux-saints/{code}")
	@Operation(summary = "Mettre a jour un lieu spirituel")
	public LieuSaint updateLieu(@PathVariable Long code, @RequestBody LieuSaintRequest request) {
		return service.updateLieu(code, request);
	}

	@DeleteMapping("/lieux-saints/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un lieu spirituel")
	public void deleteLieu(@PathVariable Long code) {
		service.deleteLieu(code);
	}

	@PostMapping("/objets-sacres")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un objet sacre")
	public ObjetSacre createObjet(@RequestBody ObjetSacreRequest request) {
		return service.createObjet(request);
	}

	@PutMapping("/objets-sacres/{code}")
	@Operation(summary = "Mettre a jour un objet sacre")
	public ObjetSacre updateObjet(@PathVariable Long code, @RequestBody ObjetSacreRequest request) {
		return service.updateObjet(code, request);
	}

	@DeleteMapping("/objets-sacres/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un objet sacre")
	public void deleteObjet(@PathVariable Long code) {
		service.deleteObjet(code);
	}
}
