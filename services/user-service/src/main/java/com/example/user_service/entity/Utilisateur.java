package com.example.user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;

	private String username;
	private String noms;
	private String prenoms;

	@Column(name = "date_naissance")
	private String dateNaissance;

	@Column(name = "contact_1")
	private String contact1;

	@Column(name = "contact_2")
	private String contact2;

	private String password;

	private String addresse;

	@Column(name = "code_createur")
	private String codeCreateur;

	@Column(name = "code_modificateur")
	private String codeModificateur;

	@Column(name = "date_creation")
	private String dateCreation;

	@Column(name = "date_modification")
	private String dateModification;
}
