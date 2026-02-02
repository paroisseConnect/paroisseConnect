package com.example.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/roles")
@Tag(name = "Roles - Admin")
public class AdminRoleController {

	private final UserManagementService service;

	public AdminRoleController(UserManagementService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lister les roles")
	public List<Role> listRoles() {
		return service.listRoles();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un role")
	public Role createRole(@RequestBody RoleRequest request) {
		return service.createRole(request);
	}

	@PutMapping("/{code}")
	@Operation(summary = "Mettre a jour un role")
	public Role updateRole(@PathVariable Integer code, @RequestBody RoleRequest request) {
		return service.updateRole(code, request);
	}

	@DeleteMapping("/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Supprimer un role")
	public void deleteRole(@PathVariable Integer code) {
		service.deleteRole(code);
	}

	@GetMapping("/{code}/permissions")
	@Operation(summary = "Lister les permissions d'un role")
	public List<Integer> listRolePermissions(@PathVariable Integer code) {
		return service.listRolePermissions(code);
	}

	@PutMapping("/{code}/permissions")
	@Operation(summary = "Mettre a jour les permissions d'un role")
	public void updateRolePermissions(@PathVariable Integer code, @RequestBody RolePermissionsRequest request) {
		service.updateRolePermissions(code, request);
	}
}
