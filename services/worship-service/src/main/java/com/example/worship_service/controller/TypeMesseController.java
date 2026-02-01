package com.example.worship_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.worship_service.dto.TypeMesseDto;
import com.example.worship_service.service.HoraireMesseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/types-messe")
@Tag(name = "Horaires Messes - Types")
public class TypeMesseController {

	private final HoraireMesseService horaireService;

	public TypeMesseController(HoraireMesseService horaireService) {
		this.horaireService = horaireService;
	}

	@GetMapping
	@Operation(summary = "Lister les types de messe")
	public List<TypeMesseDto> listTypes() {
		return horaireService.listTypesMesse();
	}
}
