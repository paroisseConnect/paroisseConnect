package com.example.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Users")
public class UserController {

	private final UserManagementService service;

	public UserController(UserManagementService service) {
		this.service = service;
	}

	@GetMapping("/users/me")
	@Operation(summary = "Obtenir le profil du fidele connecte")
	public UserDto me(@RequestHeader("X-User-Id") Integer userId) {
		return service.getUser(userId);
	}

	@PatchMapping("/users/me")
	@Operation(summary = "Mettre a jour le profil du fidele")
	public UserDto updateMe(@RequestHeader("X-User-Id") Integer userId,
			@RequestBody UserUpdateRequest request) {
		return service.updateUser(userId, request);
	}

	@GetMapping("/subscriptions")
	@Operation(summary = "Lister les abonnements du fidele")
	public List<AbonnementUniteEcclesiale> listSubscriptions(@RequestHeader("X-User-Id") Integer userId) {
		return service.listSubscriptions(userId);
	}

	@PostMapping("/subscriptions")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "S'abonner a une paroisse")
	public AbonnementUniteEcclesiale subscribe(@RequestHeader("X-User-Id") Integer userId,
			@RequestBody SubscriptionRequest request) {
		return service.createSubscription(userId, request);
	}

	@DeleteMapping("/subscriptions/{code}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Se desabonner d'une paroisse")
	public void unsubscribe(@PathVariable Integer code) {
		service.deleteSubscription(code);
	}
}
