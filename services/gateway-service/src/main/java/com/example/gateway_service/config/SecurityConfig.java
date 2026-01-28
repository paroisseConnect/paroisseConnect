package com.example.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuration de sécurité pour la Gateway.
 * Pour l'instant, désactive la sécurité pour permettre le développement.
 * À remplacer par une configuration Keycloak plus tard.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeExchange(exchanges -> exchanges
				.anyExchange().permitAll()
			);
		return http.build();
	}
}
