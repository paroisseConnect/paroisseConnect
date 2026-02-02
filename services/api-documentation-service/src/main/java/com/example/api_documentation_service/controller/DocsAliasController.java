package com.example.api_documentation_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/docs")
@Tag(name = "Documentation")
public class DocsAliasController {

	private final DiscoveryClient discoveryClient;

	public DocsAliasController(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	@GetMapping("/urls")
	@Operation(summary = "Liste structuree des URLs Swagger")
	public ResponseEntity<Map<String, Object>> listSwaggerUrls() {
		List<String> services = discoveryClient.getServices();
		Map<String, String> swaggerUrls = services.stream()
				.collect(Collectors.toMap(
						serviceName -> serviceName,
						serviceName -> String.format("http://localhost:8080/%s/v3/api-docs",
								serviceName.toLowerCase())));
		Map<String, Object> response = new HashMap<>();
		response.put("swaggerUrls", swaggerUrls);
		response.put("uiUrl", "http://localhost:8080/swagger-ui.html");
		return ResponseEntity.ok(response);
	}
}
