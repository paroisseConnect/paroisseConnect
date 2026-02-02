package com.example.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.SubscriptionRequest;
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
@RequestMapping("/api")
@Tag(name = "Users", description = "Ressources fidèle : profil et abonnements paroissiaux")
@PreAuthorize("hasRole('USER')")
public class UserController {

	private final UserManagementService service;

	public UserController(UserManagementService service) {
		this.service = service;
	}

	@GetMapping("/users/me")
	@Operation(summary = "Obtenir le profil du fidèle connecté", description = "Retourne le profil de l'utilisateur authentifié. Rôle requis : USER.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Profil trouvé", content = @Content(schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "401", description = "Non authentifié (header X-User-Id ou Bearer manquant)"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (USER requis)")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public UserDto me(@RequestHeader("X-User-Id") Integer userId) {
		return service.getUser(userId);
	}

	@PatchMapping("/users/me")
	@Operation(summary = "Mettre à jour le profil du fidèle", description = "Met à jour les informations du fidèle connecté. Rôle requis : USER.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Profil mis à jour", content = @Content(schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (USER requis)")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public UserDto updateMe(@RequestHeader("X-User-Id") Integer userId,
			@RequestBody UserUpdateRequest request) {
		return service.updateUser(userId, request);
	}

	@GetMapping("/subscriptions")
	@Operation(summary = "Lister les abonnements du fidèle", description = "Liste les paroisses auxquelles le fidèle est abonné. Rôle requis : USER.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Liste des abonnements"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (USER requis)")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public List<AbonnementUniteEcclesiale> listSubscriptions(@RequestHeader("X-User-Id") Integer userId) {
		return service.listSubscriptions(userId);
	}

	@PostMapping("/subscriptions")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "S'abonner à une paroisse", description = "Crée un abonnement à une unité ecclésiale. Rôle requis : USER.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Abonnement créé"),
			@ApiResponse(responseCode = "400", description = "Données invalides (code ou paroisse manquant)"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (USER requis)"),
			@ApiResponse(responseCode = "409", description = "Code d'abonnement déjà utilisé")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public AbonnementUniteEcclesiale subscribe(@RequestHeader("X-User-Id") Integer userId,
			@RequestBody SubscriptionRequest request) {
		return service.createSubscription(userId, request);
	}

	@DeleteMapping("/subscriptions/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Se désabonner d'une paroisse", description = "Supprime un abonnement du fidèle connecté (uniquement ses propres abonnements). Rôle requis : USER.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Abonnement supprimé"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (USER requis)"),
			@ApiResponse(responseCode = "404", description = "Abonnement introuvable ou non autorisé")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public void unsubscribe(@RequestHeader("X-User-Id") Integer userId, @PathVariable Integer code) {
		service.deleteSubscriptionByUser(code, userId);
	}
}
