package com.example.communication_service.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configure les URLs de serveur dans le spec OpenAPI pour que le "Try it out"
 * de Swagger UI fonctionne correctement :
 * - Via la gateway : utiliser /communication-service (requêtes passent par la gateway).
 * - En direct : utiliser / pour tester le service seul sur son port.
 */
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.servers(List.of(
						new Server().url("/communication-service").description("Via Gateway"),
						new Server().url("/").description("Direct (service seul)")));
	}
}
