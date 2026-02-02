package com.example.user_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleUtilisateur;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.RoleUtilisateurRepository;
import com.example.user_service.repository.UtilisateurRepository;

/**
 * Résolution des rôles utilisateur pour le contrôle d'accès.
 * Tout utilisateur authentifié reçoit ROLE_USER ; les rôles dont le libellé est "ADMIN" donnent ROLE_ADMIN.
 */
@Service
public class UserRoleService {

	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ADMIN_LIBELLE = "ADMIN";

	private final UtilisateurRepository utilisateurRepository;
	private final RoleUtilisateurRepository roleUtilisateurRepository;
	private final RoleRepository roleRepository;

	public UserRoleService(UtilisateurRepository utilisateurRepository,
			RoleUtilisateurRepository roleUtilisateurRepository,
			RoleRepository roleRepository) {
		this.utilisateurRepository = utilisateurRepository;
		this.roleUtilisateurRepository = roleUtilisateurRepository;
		this.roleRepository = roleRepository;
	}

	@Transactional(readOnly = true)
	public List<GrantedAuthority> getAuthoritiesForUser(Integer userId) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (userId == null || !utilisateurRepository.existsById(userId)) {
			return authorities;
		}
		authorities.add(new SimpleGrantedAuthority(ROLE_USER));
		List<RoleUtilisateur> links = roleUtilisateurRepository.findByCodeUtilisateur(userId);
		for (RoleUtilisateur link : links) {
			Role role = roleRepository.findById(link.getCodeRole()).orElse(null);
			if (role != null && ADMIN_LIBELLE.equalsIgnoreCase(role.getLibelle())) {
				authorities.add(new SimpleGrantedAuthority(ROLE_ADMIN));
				break;
			}
		}
		return authorities;
	}
}
