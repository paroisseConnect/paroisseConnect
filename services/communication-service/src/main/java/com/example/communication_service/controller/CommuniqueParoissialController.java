package com.example.communication_service.controller;

import java.time.LocalDate;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.communication_service.dto.CommuniqueDetailDto;
import com.example.communication_service.dto.CommuniqueRequest;
import com.example.communication_service.dto.CommuniqueSummaryDto;
import com.example.communication_service.service.CommuniqueParoissialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/communiques")
@Validated
@Tag(name = "Communiques")
public class CommuniqueParoissialController {

	private final CommuniqueParoissialService communiqueService;

	public CommuniqueParoissialController(CommuniqueParoissialService communiqueService) {
		this.communiqueService = communiqueService;
	}

	@GetMapping
	@Operation(summary = "Lister les communiques avec filtres")
	public Page<CommuniqueSummaryDto> listCommuniques(
			@Parameter(description = "Code de l'unite ecclesiale")
			@RequestParam(required = false) Long codeUniteEcclesiale,
			@Parameter(description = "Code du type de communique")
			@RequestParam(required = false) Long codeType,
			@Parameter(description = "Date debut de publication")
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dateDebut,
			@Parameter(description = "Date fin de publication")
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dateFin,
			@Parameter(description = "Filtrer par actif")
			@RequestParam(required = false) Boolean actif,
			@ParameterObject Pageable pageable) {
		return communiqueService.listCommuniques(codeUniteEcclesiale, codeType, dateDebut, dateFin, actif, pageable);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtenir le detail d'un communique")
	public CommuniqueDetailDto getCommunique(@PathVariable Long id) {
		return communiqueService.getCommunique(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un communique")
	public CommuniqueDetailDto createCommunique(@Valid @RequestBody CommuniqueRequest request) {
		return communiqueService.create(request);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Mettre a jour un communique")
	public CommuniqueDetailDto updateCommunique(@PathVariable Long id, @Valid @RequestBody CommuniqueRequest request) {
		return communiqueService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un communique")
	public void deleteCommunique(@PathVariable Long id) {
		communiqueService.delete(id);
	}
}
