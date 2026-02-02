package com.example.user_service.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.UserDto;
import com.example.user_service.dto.UserUpdateRequest;
import com.example.user_service.entity.AbonnementUniteEcclesiale;
import com.example.user_service.service.UserManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Users - Admin", description = "Gestion des utilisateurs et abonnements (équipe paroissiale)")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

	private final UserManagementService service;

	public AdminUserController(UserManagementService service) {
		this.service = service;
	}

	@GetMapping("/users")
	@Operation(summary = "Lister les utilisateurs", description = "Liste tous les utilisateurs. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Liste des utilisateurs"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public List<UserDto> listUsers() {
		return service.listUsers();
	}

	@GetMapping("/users/{code}")
	@Operation(summary = "Détail d'un utilisateur", description = "Retourne le profil d'un utilisateur par son code. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Utilisateur trouvé", content = @Content(schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)"),
			@ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public UserDto getUser(@PathVariable Integer code) {
		return service.getUser(code);
	}

	@PatchMapping("/users/{code}")
	@Operation(summary = "Mettre à jour un utilisateur", description = "Modifie le profil d'un utilisateur. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Utilisateur mis à jour", content = @Content(schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)"),
			@ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public UserDto updateUser(@PathVariable Integer code, @RequestBody UserUpdateRequest request) {
		return service.updateUser(code, request);
	}

	@GetMapping("/subscriptions")
	@Operation(summary = "Lister les abonnements", description = "Vue globale des abonnements par paroisse. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Liste des abonnements"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public List<AbonnementUniteEcclesiale> listSubscriptions() {
		return service.listAllSubscriptions();
	}
}
