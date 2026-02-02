package com.example.user_service.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Génération de JWT au login (claim "sub" = code utilisateur pour X-User-Id côté gateway).
 */
@Service
public class JwtService {

	private final SecretKey key;
	private final long expirationMs;

	public JwtService(
			@Value("${jwt.secret:paroisse-connect-jwt-secret-change-in-production}") String secret,
			@Value("${jwt.expiration-ms:86400000}") long expirationMs) {
		byte[] raw = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
		// HS256 exige au moins 32 octets
		if (raw.length < 32) {
			byte[] padded = new byte[32];
			System.arraycopy(raw, 0, padded, 0, raw.length);
			this.key = Keys.hmacShaKeyFor(padded);
		} else {
			this.key = Keys.hmacShaKeyFor(raw);
		}
		this.expirationMs = expirationMs;
	}

	public String createToken(Integer userId, String username) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + expirationMs);
		return Jwts.builder()
				.subject(String.valueOf(userId))
				.claim("username", username)
				.issuedAt(now)
				.expiration(exp)
				.signWith(key)
				.compact();
	}
}
