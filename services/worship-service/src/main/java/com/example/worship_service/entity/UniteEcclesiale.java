package com.example.worship_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "unite_ecclesiale", schema = "paroisse_schema")
@Getter
@Setter
@NoArgsConstructor
public class UniteEcclesiale {

	@Id
	@Column(name = "code")
	private Long code;

	@Column(name = "libelle")
	private String libelle;

	@Column(name = "description")
	private String description;

	@Column(name = "titre")
	private String titre;

	@Column(name = "adresse")
	private String adresse;

	@Column(name = "code_type")
	private Long codeType;

	@Column(name = "longitude")
	private String longitude;

	@Column(name = "latitude")
	private String latitude;

	@Column(name = "altitude")
	private String altitude;

	@Column(name = "fidele")
	private String fidele;

	@Column(name = "date_creation_unite")
	private LocalDateTime dateCreationUnite;

	@Column(name = "statut_canonique")
	private String statutCanonique;

	@Column(name = "code_unite_parent")
	private Long codeUniteParent;

	@Column(name = "activite")
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
