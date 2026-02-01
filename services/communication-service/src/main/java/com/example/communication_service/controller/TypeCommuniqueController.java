package com.example.communication_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.communication_service.dto.TypeCommuniqueDto;
import com.example.communication_service.service.CommuniqueParoissialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/types-communique")
@Tag(name = "Communiques - Types")
public class TypeCommuniqueController {

	private final CommuniqueParoissialService communiqueService;

	public TypeCommuniqueController(CommuniqueParoissialService communiqueService) {
		this.communiqueService = communiqueService;
	}

	@GetMapping
	@Operation(summary = "Lister les types de communique")
	public List<TypeCommuniqueDto> listTypes() {
		return communiqueService.listTypes();
	}
}
