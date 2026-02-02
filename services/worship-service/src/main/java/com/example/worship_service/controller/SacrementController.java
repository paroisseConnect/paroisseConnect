package com.example.worship_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.worship_service.entity.Confession;
import com.example.worship_service.entity.IntentionMesse;
import com.example.worship_service.service.SacrementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Sacrements")
public class SacrementController {

	private final SacrementService service;

	public SacrementController(SacrementService service) {
		this.service = service;
	}

	@GetMapping("/confessions")
	@Operation(summary = "Lister les horaires de confession")
	public List<Confession> listConfessions() {
		return service.listConfessions();
	}

	@GetMapping("/confessions/{code}")
	@Operation(summary = "Detail d'un creneau de confession")
	public Confession getConfession(@PathVariable Long code) {
		return service.getConfession(code);
	}

	@GetMapping("/intentions")
	@Operation(summary = "Lister les intentions de messe")
	public List<IntentionMesse> listIntentions() {
		return service.listIntentions();
	}
}
