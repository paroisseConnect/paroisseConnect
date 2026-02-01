package com.example.worship_service.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	@ConditionalOnProperty(name = "security.oauth2.enabled", havingValue = "true")
	public SecurityFilterChain securedSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/swagger-ui.html",
					"/swagger-ui/**",
					"/v3/api-docs/**",
					"/actuator/**")
				.permitAll()
				.anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth2 -> oauth2.jwt());
		return http.build();
	}

	@Bean
	@ConditionalOnProperty(name = "security.oauth2.enabled", havingValue = "false", matchIfMissing = true)
	public SecurityFilterChain openSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		return http.build();
	}
}
