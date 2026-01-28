package com.example.api_documentation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contrôleur pour gérer la documentation des services.
 * Fournit des informations sur tous les services enregistrés dans Eureka.
 */
@RestController
@RequestMapping("/api/documentation")
@Tag(name = "Documentation", description = "Gestion de la documentation des services")
public class DocumentationController {

	private final DiscoveryClient discoveryClient;

	public DocumentationController(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	@GetMapping("/services")
	@Operation(summary = "Liste tous les services disponibles", 
			   description = "Retourne la liste de tous les services enregistrés dans Eureka avec leurs informations")
	public ResponseEntity<Map<String, Object>> getServices() {
		List<String> services = discoveryClient.getServices();
		
		Map<String, Object> response = new HashMap<>();
		response.put("total", services.size());
		response.put("services", services.stream()
				.map(serviceName -> {
					Map<String, Object> serviceInfo = new HashMap<>();
					serviceInfo.put("name", serviceName);
					
					List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
					serviceInfo.put("instances", instances.size());
					
					if (!instances.isEmpty()) {
						ServiceInstance instance = instances.get(0);
						Map<String, Object> instanceInfo = new HashMap<>();
						instanceInfo.put("host", instance.getHost());
						instanceInfo.put("port", instance.getPort());
						instanceInfo.put("uri", instance.getUri().toString());
						instanceInfo.put("serviceId", instance.getServiceId());
						
						// URL de la documentation Swagger pour ce service
						// Via Gateway (pour les services Spring MVC avec Swagger UI)
						String swaggerUiUrl = String.format("http://localhost:8080/%s/swagger-ui.html", 
								serviceName.toLowerCase());
						String swaggerApiDocsUrl = String.format("http://localhost:8080/%s/v3/api-docs", 
								serviceName.toLowerCase());
						instanceInfo.put("swaggerUiUrl", swaggerUiUrl);
						instanceInfo.put("swaggerApiDocsUrl", swaggerApiDocsUrl);
						// URL directe (si le service est accessible directement)
						String directSwaggerUiUrl = String.format("http://%s:%d/swagger-ui.html", 
								instance.getHost(), instance.getPort());
						instanceInfo.put("directSwaggerUiUrl", directSwaggerUiUrl);
						
						serviceInfo.put("details", instanceInfo);
					}
					
					return serviceInfo;
				})
				.collect(Collectors.toList()));
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("/swagger-urls")
	@Operation(summary = "Liste toutes les URLs Swagger", 
			   description = "Retourne les URLs de documentation Swagger pour chaque service")
	public ResponseEntity<Map<String, String>> getSwaggerUrls() {
		List<String> services = discoveryClient.getServices();
		
		Map<String, String> swaggerUrls = services.stream()
				.collect(Collectors.toMap(
						serviceName -> serviceName,
						serviceName -> String.format("http://localhost:8080/%s/v3/api-docs", 
								serviceName.toLowerCase())
				));
		
		Map<String, Object> response = new HashMap<>();
		response.put("swaggerUrls", swaggerUrls);
		response.put("uiUrl", "http://localhost:8080/api-documentation-service/swagger-ui.html");
		response.put("aggregatedApiDocs", "http://localhost:8080/api-documentation-service/v3/api-docs");
		response.put("note", "Pour accéder à Swagger UI d'un service, utilisez: http://localhost:8080/{service-name}/swagger-ui.html");
		response.put("directAccessNote", "Vous pouvez aussi accéder directement aux services sur leurs ports respectifs");
		
		return ResponseEntity.ok((Map) response);
	}
}
