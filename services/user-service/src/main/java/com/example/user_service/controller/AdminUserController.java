package com.example.user_service.controller;

import java.util.List;

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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Users - Admin")
public class AdminUserController {

	private final UserManagementService service;

	public AdminUserController(UserManagementService service) {
		this.service = service;
	}

	@GetMapping("/users")
	@Operation(summary = "Lister les utilisateurs")
	public List<UserDto> listUsers() {
		return service.listUsers();
	}

	@GetMapping("/users/{code}")
	@Operation(summary = "Detail d'un utilisateur")
	public UserDto getUser(@PathVariable Integer code) {
		return service.getUser(code);
	}

	@PatchMapping("/users/{code}")
	@Operation(summary = "Mettre a jour un utilisateur")
	public UserDto updateUser(@PathVariable Integer code, @RequestBody UserUpdateRequest request) {
		return service.updateUser(code, request);
	}

	@GetMapping("/subscriptions")
	@Operation(summary = "Lister les abonnements")
	public List<AbonnementUniteEcclesiale> listSubscriptions() {
		return service.listAllSubscriptions();
	}
}
