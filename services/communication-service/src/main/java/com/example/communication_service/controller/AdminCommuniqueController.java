package com.example.communication_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.communication_service.dto.CommuniqueDetailDto;
import com.example.communication_service.dto.CommuniqueRequest;
import com.example.communication_service.service.CommuniqueParoissialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Communiques - Admin")
public class AdminCommuniqueController {

	private final CommuniqueParoissialService communiqueService;

	public AdminCommuniqueController(CommuniqueParoissialService communiqueService) {
		this.communiqueService = communiqueService;
	}

	@PostMapping("/communiques")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un communique")
	public CommuniqueDetailDto create(@Valid @RequestBody CommuniqueRequest request) {
		return communiqueService.create(request);
	}

	@PutMapping("/communiques/{id}")
	@Operation(summary = "Mettre a jour un communique")
	public CommuniqueDetailDto update(@PathVariable Long id, @Valid @RequestBody CommuniqueRequest request) {
		return communiqueService.update(id, request);
	}

	@DeleteMapping("/communiques/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un communique")
	public void delete(@PathVariable Long id) {
		communiqueService.delete(id);
	}

	@PostMapping("/communiques/{id}/attachments")
	@Operation(summary = "Ajouter une piece jointe")
	public CommuniqueDetailDto addAttachment(@PathVariable Long id,
			@Valid @RequestBody CommuniqueRequest.PieceJointeRequest request) {
		return communiqueService.addAttachment(id, request);
	}

	@DeleteMapping("/communiques/{id}/attachments/{pj}")
	@Operation(summary = "Supprimer une piece jointe")
	public CommuniqueDetailDto removeAttachment(@PathVariable Long id, @PathVariable("pj") Long pieceJointeId) {
		return communiqueService.removeAttachment(id, pieceJointeId);
	}

	@PostMapping("/notifications/communiques/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "Declencher une notification push pour un communique")
	public void notifyCommunique(@PathVariable Long id) {
		communiqueService.triggerNotification(id);
	}
}
