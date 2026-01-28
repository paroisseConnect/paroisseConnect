package com.example.api_documentation_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI pour la documentation centralisée de tous les services.
 */
@Configuration
public class OpenApiConfig {

	@Value("${server.port:8087}")
	private String serverPort;

	@Bean
	public OpenAPI paroisseConnectOpenAPI() {
		Server gatewayServer = new Server();
		gatewayServer.setUrl("http://localhost:8080");
		gatewayServer.setDescription("Gateway Server - Point d'entrée unique");

		Server localServer = new Server();
		localServer.setUrl("http://localhost:" + serverPort);
		localServer.setDescription("API Documentation Service");

		Contact contact = new Contact();
		contact.setName("Paroisse Connect Team");
		contact.setEmail("dev@paroisseconnect.com");

		License license = new License();
		license.setName("Proprietary");
		license.setUrl("https://paroisseconnect.com/license");

		Info info = new Info()
				.title("Paroisse Connect API Documentation")
				.version("1.0.0")
				.description("Documentation centralisée de tous les microservices de Paroisse Connect. " +
						"Cette documentation agrège les endpoints de tous les services disponibles dans l'écosystème.")
				.contact(contact)
				.license(license);

		return new OpenAPI()
				.info(info)
				.servers(List.of(gatewayServer, localServer));
	}
}
