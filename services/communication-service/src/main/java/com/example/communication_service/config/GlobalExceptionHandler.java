package com.example.communication_service.config;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * Gère les erreurs multipart (corps vide, stream tronqué, etc.) souvent causées
 * par un client qui envoie Content-Type multipart sans corps valide (ex. Swagger sans fichier).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<Map<String, String>> handleMultipartException(MultipartException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of(
						"error", "Bad Request",
						"message", "Requête multipart invalide ou corps vide. Vérifiez que le corps de la requête est bien formé (boundary, parties)."));
	}
}
