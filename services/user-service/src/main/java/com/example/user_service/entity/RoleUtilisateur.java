package com.example.user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role_utilisateur")
@Getter
@Setter
@NoArgsConstructor
public class RoleUtilisateur {

	@Id
	private Integer code;

	@Column(name = "code_utilisateur")
	private Integer codeUtilisateur;

	@Column(name = "code_role")
	private Integer codeRole;
}
