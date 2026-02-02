package com.example.user_service.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import com.example.user_service.service.UserRoleService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public XUserIdAuthenticationFilter xUserIdAuthenticationFilter(UserRoleService userRoleService) {
		return new XUserIdAuthenticationFilter(userRoleService);
	}

	@Bean
	@ConditionalOnProperty(name = "security.oauth2.enabled", havingValue = "true")
	public SecurityFilterChain securedSecurityFilterChain(HttpSecurity http,
			XUserIdAuthenticationFilter xUserIdAuthenticationFilter) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.addFilterBefore(xUserIdAuthenticationFilter, AnonymousAuthenticationFilter.class)
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
	public SecurityFilterChain openSecurityFilterChain(HttpSecurity http,
			XUserIdAuthenticationFilter xUserIdAuthenticationFilter) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.addFilterBefore(xUserIdAuthenticationFilter, AnonymousAuthenticationFilter.class)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**")
				.permitAll()
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/users/**", "/api/subscriptions/**").hasRole("USER")
				.anyRequest().authenticated()
			);
		return http.build();
	}
}
