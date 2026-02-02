package com.example.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.RolePermissionsRequest;
import com.example.user_service.dto.RoleRequest;
import com.example.user_service.entity.Role;
import com.example.user_service.service.UserManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/roles")
@Tag(name = "Roles - Admin", description = "Gestion des rôles et permissions (équipe paroissiale)")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoleController {

	private final UserManagementService service;

	public AdminRoleController(UserManagementService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lister les rôles", description = "Liste tous les rôles disponibles. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Liste des rôles"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public List<Role> listRoles() {
		return service.listRoles();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Créer un rôle", description = "Crée un nouveau rôle. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Rôle créé", content = @Content(schema = @Schema(implementation = Role.class))),
			@ApiResponse(responseCode = "400", description = "Code obligatoire ou invalide"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)"),
			@ApiResponse(responseCode = "409", description = "Code de rôle déjà utilisé")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public Role createRole(@RequestBody RoleRequest request) {
		return service.createRole(request);
	}

	@PutMapping("/{code}")
	@Operation(summary = "Mettre à jour un rôle", description = "Modifie un rôle existant. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Rôle mis à jour", content = @Content(schema = @Schema(implementation = Role.class))),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)"),
			@ApiResponse(responseCode = "404", description = "Rôle introuvable")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public Role updateRole(@PathVariable Integer code, @RequestBody RoleRequest request) {
		return service.updateRole(code, request);
	}

	@DeleteMapping("/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un rôle", description = "Supprime un rôle. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Rôle supprimé"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)"),
			@ApiResponse(responseCode = "404", description = "Rôle introuvable")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public void deleteRole(@PathVariable Integer code) {
		service.deleteRole(code);
	}

	@GetMapping("/{code}/permissions")
	@Operation(summary = "Lister les permissions d'un rôle", description = "Retourne les codes des permissions associées à un rôle. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Liste des codes de permissions"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public List<Integer> listRolePermissions(@PathVariable Integer code) {
		return service.listRolePermissions(code);
	}

	@PutMapping("/{code}/permissions")
	@Operation(summary = "Mettre à jour les permissions d'un rôle", description = "Associe ou dissocie des permissions à un rôle. Rôle requis : ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Permissions mises à jour"),
			@ApiResponse(responseCode = "400", description = "Permission invalide"),
			@ApiResponse(responseCode = "401", description = "Non authentifié"),
			@ApiResponse(responseCode = "403", description = "Rôle insuffisant (ADMIN requis)"),
			@ApiResponse(responseCode = "404", description = "Rôle introuvable")
	})
	@SecurityRequirement(name = "bearerAuth")
	@SecurityRequirement(name = "X-User-Id")
	public void updateRolePermissions(@PathVariable Integer code, @RequestBody RolePermissionsRequest request) {
		service.updateRolePermissions(code, request);
	}
}
