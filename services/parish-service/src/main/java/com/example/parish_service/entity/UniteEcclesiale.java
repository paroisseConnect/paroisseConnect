package com.example.parish_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "unite_ecclesiale")
@Getter
@Setter
@NoArgsConstructor
public class UniteEcclesiale {

	@Id
	private Long code;

	private String libelle;

	private String description;

	private String titre;

	private String adresse;

	@Column(name = "code_type")
	private Long codeType;

	private String longitude;

	private String latitude;

	private String altitude;

	private String fidele;

	@Column(name = "date_creation_unite")
	private LocalDateTime dateCreationUnite;

	@Column(name = "statut_canonique")
	private String statutCanonique;

	@Column(name = "code_unite_parent")
	private Long codeUniteParent;

	private String activite;

	@Column(name = "date_creation")
	private LocalDateTime dateCreation;

	@Column(name = "date_modification")
	private LocalDateTime dateModification;

	@Column(name = "code_createur")
	private Long codeCreateur;

	@Column(name = "code_modificateur")
	private Long codeModificateur;
}
