package com.example.worship_service.controller;

import java.time.LocalDate;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.worship_service.dto.HoraireMesseDetailDto;
import com.example.worship_service.dto.HoraireMesseOccurrenceDto;
import com.example.worship_service.service.HoraireMesseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping({"/api/horaires-messes", "/api/messes"})
@Validated
@Tag(name = "Horaires Messes")
public class HoraireMesseController {

	private final HoraireMesseService horaireService;

	public HoraireMesseController(HoraireMesseService horaireService) {
		this.horaireService = horaireService;
	}

	@GetMapping
	@Operation(summary = "Lister les horaires des messes")
	public Page<HoraireMesseOccurrenceDto> listHoraires(
			@Parameter(description = "Date specifique")
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate date,
			@Parameter(description = "Jour de semaine (1-7)")
			@RequestParam(required = false) Integer jourSemaine,
			@Parameter(description = "Date debut de recherche")
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dateDebut,
			@Parameter(description = "Date fin de recherche")
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dateFin,
			@Parameter(description = "Code type messe")
			@RequestParam(required = false) String codeTypeMesse,
			@Parameter(description = "Code unite ecclesiale")
			@RequestParam(required = false) Long codeUniteEcclesiale,
			@ParameterObject Pageable pageable) {
		return horaireService.listHoraires(date, jourSemaine, dateDebut, dateFin, codeTypeMesse, codeUniteEcclesiale,
				pageable);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtenir le detail d'un horaire")
	public HoraireMesseDetailDto getHoraire(
			@PathVariable Long id,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
		return horaireService.getHoraire(id, date);
	}

}
