package com.example.user_service.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.user_service.service.UserRoleService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtre qui lit le header X-User-Id (posé par la gateway après validation JWT),
 * charge les rôles de l'utilisateur et alimente le SecurityContext pour @PreAuthorize.
 */
public class XUserIdAuthenticationFilter extends OncePerRequestFilter {

	private final UserRoleService userRoleService;

	public XUserIdAuthenticationFilter(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader("X-User-Id");
		if (header != null && !header.isBlank()) {
			try {
				Integer userId = Integer.valueOf(header.trim());
				List<GrantedAuthority> authorities = userRoleService.getAuthoritiesForUser(userId);
				if (!authorities.isEmpty()) {
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							userId, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (NumberFormatException ignored) {
				// X-User-Id invalide : on laisse anonyme
			}
		}
		filterChain.doFilter(request, response);
	}
}
