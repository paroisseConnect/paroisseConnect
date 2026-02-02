package com.example.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.LoginResponse;
import com.example.user_service.dto.RegisterRequest;
import com.example.user_service.dto.UserDto;
import com.example.user_service.service.UserManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Inscription et authentification (accès public)")
@SecurityRequirements
public class AuthController {

	private final UserManagementService service;

	public AuthController(UserManagementService service) {
		this.service = service;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Créer un compte", description = "Inscription d'un nouveau fidèle. Aucune authentification requise.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Compte créé", content = @Content(schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "400", description = "Données invalides (username ou mot de passe manquant)"),
			@ApiResponse(responseCode = "409", description = "Username déjà utilisé")
	})
	public UserDto register(@RequestBody RegisterRequest request) {
		return service.register(request);
	}

	@PostMapping("/login")
	@Operation(summary = "Authentifier un utilisateur", description = "Connexion avec username et mot de passe. Retourne un token JWT à utiliser dans Authorization: Bearer.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Authentification réussie", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
			@ApiResponse(responseCode = "401", description = "Identifiants invalides")
	})
	public LoginResponse login(@RequestBody LoginRequest request) {
		return service.login(request);
	}
}
