package com.example.user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "abonnement_unite_ecclesiale")
@Getter
@Setter
@NoArgsConstructor
public class AbonnementUniteEcclesiale {

	@Id
	private Integer code;

	@Column(name = "code_unite_ecclesiale")
	private Integer codeUniteEcclesiale;

	@Column(name = "code_utilisateur")
	private Integer codeUtilisateur;
}
