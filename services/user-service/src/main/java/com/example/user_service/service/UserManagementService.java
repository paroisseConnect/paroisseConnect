package com.example.user_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.user_service.config.JwtService;
import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.LoginResponse;
import com.example.user_service.dto.RegisterRequest;
import com.example.user_service.dto.RolePermissionsRequest;
import com.example.user_service.dto.RoleRequest;
import com.example.user_service.dto.SubscriptionRequest;
import com.example.user_service.dto.UserDto;
import com.example.user_service.dto.UserUpdateRequest;
import com.example.user_service.entity.AbonnementUniteEcclesiale;
import com.example.user_service.entity.Permission;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.RolePermission;
import com.example.user_service.entity.Utilisateur;
import com.example.user_service.repository.AbonnementRepository;
import com.example.user_service.repository.PermissionRepository;
import com.example.user_service.repository.RolePermissionRepository;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UtilisateurRepository;

@Service
@Transactional
public class UserManagementService {

	private final UtilisateurRepository utilisateurRepository;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final RolePermissionRepository rolePermissionRepository;
	private final AbonnementRepository abonnementRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public UserManagementService(UtilisateurRepository utilisateurRepository,
			RoleRepository roleRepository,
			PermissionRepository permissionRepository,
			RolePermissionRepository rolePermissionRepository,
			AbonnementRepository abonnementRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService) {
		this.utilisateurRepository = utilisateurRepository;
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;
		this.rolePermissionRepository = rolePermissionRepository;
		this.abonnementRepository = abonnementRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public UserDto register(RegisterRequest request) {
		if (request.getUsername() == null || request.getUsername().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username obligatoire");
		}
		if (request.getPassword() == null || request.getPassword().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mot de passe obligatoire");
		}
		if (utilisateurRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username deja utilise");
		}
		Utilisateur user = new Utilisateur();
		user.setUsername(request.getUsername());
		user.setNoms(request.getNoms());
		user.setPrenoms(request.getPrenoms());
		user.setDateNaissance(request.getDateNaissance());
		user.setContact1(request.getContact1());
		user.setContact2(request.getContact2());
		user.setAddresse(request.getAddresse());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setDateCreation(LocalDateTime.now().toString());
		return UserDto.from(utilisateurRepository.save(user));
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		Utilisateur user = utilisateurRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides"));
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides");
		}
		String token = jwtService.createToken(user.getCode(), user.getUsername());
		return new LoginResponse(token, UserDto.from(user));
	}

	@Transactional(readOnly = true)
	public UserDto getUser(Integer code) {
		return UserDto.from(findUser(code));
	}

	public UserDto updateUser(Integer code, UserUpdateRequest request) {
		Utilisateur user = findUser(code);
		if (request.getNoms() != null) {
			user.setNoms(request.getNoms());
		}
		if (request.getPrenoms() != null) {
			user.setPrenoms(request.getPrenoms());
		}
		if (request.getDateNaissance() != null) {
			user.setDateNaissance(request.getDateNaissance());
		}
		if (request.getContact1() != null) {
			user.setContact1(request.getContact1());
		}
		if (request.getContact2() != null) {
			user.setContact2(request.getContact2());
		}
		if (request.getAddresse() != null) {
			user.setAddresse(request.getAddresse());
		}
		user.setDateModification(LocalDateTime.now().toString());
		return UserDto.from(utilisateurRepository.save(user));
	}

	@Transactional(readOnly = true)
	public List<UserDto> listUsers() {
		return utilisateurRepository.findAll().stream().map(UserDto::from).toList();
	}

	@Transactional(readOnly = true)
	public List<Role> listRoles() {
		return roleRepository.findAll();
	}

	public Role createRole(RoleRequest request) {
		if (request.getCode() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code obligatoire");
		}
		if (roleRepository.existsById(request.getCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Code deja utilise");
		}
		Role role = new Role();
		apply(role, request);
		return roleRepository.save(role);
	}

	public Role updateRole(Integer code, RoleRequest request) {
		Role role = roleRepository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role introuvable"));
		apply(role, request);
		role.setCode(code);
		return roleRepository.save(role);
	}

	public void deleteRole(Integer code) {
		if (!roleRepository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role introuvable");
		}
		roleRepository.deleteById(code);
	}

	@Transactional(readOnly = true)
	public List<Integer> listRolePermissions(Integer codeRole) {
		return rolePermissionRepository.findByCodeRole(codeRole).stream()
				.map(RolePermission::getCodePermission)
				.toList();
	}

	public void updateRolePermissions(Integer codeRole, RolePermissionsRequest request) {
		if (!roleRepository.existsById(codeRole)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role introuvable");
		}
		rolePermissionRepository.deleteByCodeRole(codeRole);
		if (request.getPermissionCodes() == null) {
			return;
		}
		for (Integer permissionCode : request.getPermissionCodes()) {
			Permission permission = permissionRepository.findById(permissionCode)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Permission invalide"));
			RolePermission link = new RolePermission();
			link.setCodeRole(codeRole);
			link.setCodePermission(permission.getCode());
			rolePermissionRepository.save(link);
		}
	}

	@Transactional(readOnly = true)
	public List<AbonnementUniteEcclesiale> listSubscriptions(Integer userCode) {
		return abonnementRepository.findByCodeUtilisateur(userCode);
	}

	public AbonnementUniteEcclesiale createSubscription(Integer userCode, SubscriptionRequest request) {
		if (request.getCode() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code obligatoire");
		}
		if (request.getCodeUniteEcclesiale() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paroisse obligatoire");
		}
		if (abonnementRepository.existsById(request.getCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Code deja utilise");
		}
		AbonnementUniteEcclesiale abonnement = new AbonnementUniteEcclesiale();
		abonnement.setCode(request.getCode());
		abonnement.setCodeUniteEcclesiale(request.getCodeUniteEcclesiale());
		abonnement.setCodeUtilisateur(userCode);
		return abonnementRepository.save(abonnement);
	}

	public void deleteSubscription(Integer code) {
		if (!abonnementRepository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Abonnement introuvable");
		}
		abonnementRepository.deleteById(code);
	}

	/**
	 * Désabonnement par le fidèle : seul le propriétaire de l'abonnement peut le supprimer.
	 */
	public void deleteSubscriptionByUser(Integer code, Integer userId) {
		AbonnementUniteEcclesiale abonnement = abonnementRepository.findByCodeAndCodeUtilisateur(code, userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abonnement introuvable ou non autorise"));
		abonnementRepository.delete(abonnement);
	}

	@Transactional(readOnly = true)
	public List<AbonnementUniteEcclesiale> listAllSubscriptions() {
		return abonnementRepository.findAll();
	}

	private void apply(Role role, RoleRequest request) {
		role.setCode(request.getCode() != null ? request.getCode() : role.getCode());
		role.setLibelle(request.getLibelle());
		role.setCodeCreateur(request.getCodeCreateur());
		role.setCodeModificateur(request.getCodeModificateur());
		role.setDateCreation(request.getDateCreation());
		role.setDateModification(request.getDateModification());
	}

	private Utilisateur findUser(Integer code) {
		return utilisateurRepository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
	}
}
