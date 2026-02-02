package com.example.worship_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.worship_service.dto.ConfessionRequest;
import com.example.worship_service.dto.IntentionMesseRequest;
import com.example.worship_service.entity.Confession;
import com.example.worship_service.entity.IntentionMesse;
import com.example.worship_service.service.SacrementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Sacrements - Admin")
public class AdminSacrementController {

	private final SacrementService service;

	public AdminSacrementController(SacrementService service) {
		this.service = service;
	}

	@PostMapping("/confessions")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un creneau de confession")
	public Confession createConfession(@RequestBody ConfessionRequest request) {
		return service.createConfession(request);
	}

	@PutMapping("/confessions/{code}")
	@Operation(summary = "Mettre a jour un creneau de confession")
	public Confession updateConfession(@PathVariable Long code, @RequestBody ConfessionRequest request) {
		return service.updateConfession(code, request);
	}

	@DeleteMapping("/confessions/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un creneau de confession")
	public void deleteConfession(@PathVariable Long code) {
		service.deleteConfession(code);
	}

	@PostMapping("/intentions")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer une intention de messe")
	public IntentionMesse createIntention(@RequestBody IntentionMesseRequest request) {
		return service.createIntention(request);
	}

	@PutMapping("/intentions/{code}")
	@Operation(summary = "Mettre a jour une intention de messe")
	public IntentionMesse updateIntention(@PathVariable Long code, @RequestBody IntentionMesseRequest request) {
		return service.updateIntention(code, request);
	}

	@DeleteMapping("/intentions/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer une intention de messe")
	public void deleteIntention(@PathVariable Long code) {
		service.deleteIntention(code);
	}
}
