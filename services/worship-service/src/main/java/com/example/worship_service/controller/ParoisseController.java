package com.example.worship_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.worship_service.dto.UniteEcclesialeDto;
import com.example.worship_service.service.HoraireMesseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/paroisses")
@Tag(name = "Paroisses")
public class ParoisseController {

	private final HoraireMesseService horaireService;

	public ParoisseController(HoraireMesseService horaireService) {
		this.horaireService = horaireService;
	}

	@GetMapping
	@Operation(summary = "Lister les paroisses")
	public List<UniteEcclesialeDto> listParoisses() {
		return horaireService.listParoisses();
	}
}
