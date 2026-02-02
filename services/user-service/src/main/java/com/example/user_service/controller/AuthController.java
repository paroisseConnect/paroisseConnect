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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {

	private final UserManagementService service;

	public AuthController(UserManagementService service) {
		this.service = service;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Creer un compte")
	public UserDto register(@RequestBody RegisterRequest request) {
		return service.register(request);
	}

	@PostMapping("/login")
	@Operation(summary = "Authentifier un utilisateur")
	public LoginResponse login(@RequestBody LoginRequest request) {
		return service.login(request);
	}
}
