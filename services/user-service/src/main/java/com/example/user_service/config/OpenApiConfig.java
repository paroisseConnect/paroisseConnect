package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuration OpenAPI / Swagger pour une documentation professionnelle.
 */
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		final String bearerScheme = "bearerAuth";
		final String xUserIdScheme = "X-User-Id";
		return new OpenAPI()
				.info(new Info()
						.title("User Service — Paroisse Connect")
						.version("1.0.0")
						.description("""
								API de gestion des comptes fidèles, de l'authentification et des rôles.

								**Rôles :**
								- **USER** : accès au profil (moi) et aux abonnements paroissiaux.
								- **ADMIN** : accès à la gestion des utilisateurs, rôles et abonnements (équipe paroissiale).

								**Authentification :** après `POST /api/auth/login`, utiliser le token JWT dans `Authorization: Bearer <token>`.
								La gateway peut transmettre l'identifiant utilisateur via le header **X-User-Id**.
								""")
						.contact(new Contact()
								.name("Équipe Paroisse Connect")
								.email("contact@paroisse-connect.example.com"))
						.license(new License().name("Propriétaire").url("https://paroisse-connect.example.com/license")))
				.addServersItem(new Server().url("/").description("Service actuel"))
				.addSecurityItem(new SecurityRequirement().addList(bearerScheme))
				.addSecurityItem(new SecurityRequirement().addList(xUserIdScheme))
				.components(new Components()
						.addSecuritySchemes(bearerScheme,
								new SecurityScheme()
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")
										.description("Token JWT obtenu via POST /api/auth/login"))
						.addSecuritySchemes(xUserIdScheme,
								new SecurityScheme()
										.type(SecurityScheme.Type.APIKEY)
										.in(SecurityScheme.In.HEADER)
										.name("X-User-Id")
										.description("Identifiant utilisateur (posé par la gateway après validation JWT)")));
	}
}
