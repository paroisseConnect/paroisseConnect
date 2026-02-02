package com.example.user_service.dto;

public record LoginResponse(String token, UserDto user) {
}
