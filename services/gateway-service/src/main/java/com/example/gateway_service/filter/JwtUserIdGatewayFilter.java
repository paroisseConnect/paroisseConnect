package com.example.gateway_service.filter;

import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

/**
 * Filtre qui valide le JWT pour les requêtes vers user-service et ajoute le header X-User-Id
 * (claim "sub" = code utilisateur). Les chemins /api/auth/login et /api/auth/register sont exemptés.
 */
@Component
public class JwtUserIdGatewayFilter implements WebFilter, Ordered {

	private static final String USER_SERVICE_PREFIX = "/user-service/";
	private static final List<String> PUBLIC_PATHS = List.of(
			"/user-service/api/auth/login",
			"/user-service/api/auth/register",
			"/user-service/v3/api-docs",
			"/user-service/swagger-ui",
			"/user-service/actuator"
	);

	private final SecretKey key;

	public JwtUserIdGatewayFilter(
			@Value("${jwt.secret:paroisse-connect-jwt-secret-change-in-production}") String secret) {
		byte[] raw = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
		if (raw.length < 32) {
			byte[] padded = new byte[32];
			System.arraycopy(raw, 0, padded, 0, raw.length);
			this.key = Keys.hmacShaKeyFor(padded);
		} else {
			this.key = Keys.hmacShaKeyFor(raw);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String path = exchange.getRequest().getPath().value();
		if (!path.startsWith(USER_SERVICE_PREFIX)) {
			return chain.filter(exchange);
		}
		if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
			return chain.filter(exchange);
		}

		String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (auth == null || !auth.startsWith("Bearer ")) {
			return unauthorized(exchange.getResponse(), "Authorization Bearer requis");
		}
		String token = auth.substring(7).trim();

		String userId;
		try {
			userId = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getSubject();
		} catch (Exception e) {
			return unauthorized(exchange.getResponse(), "Token invalide ou expire");
		}

		ServerHttpRequest mutated = exchange.getRequest().mutate()
				.header("X-User-Id", userId)
				.build();
		return chain.filter(exchange.mutate().request(mutated).build());
	}

	private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		DataBuffer buffer = response.bufferFactory().wrap(("{\"error\":\"" + message + "\"}").getBytes(java.nio.charset.StandardCharsets.UTF_8));
		return response.writeWith(Mono.just(buffer));
	}
}
