package com.example.activity_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activite")
@Getter
@Setter
@NoArgsConstructor
public class Activite {

	@Id
	private Long code;

	private String libelle;

	@Column(name = "date_activite")
	private String dateActivite;

	private String description;

	private String frequence;

	@Column(name = "date_debut")
	private String dateDebut;

	@Column(name = "date_fin")
	private String dateFin;

	private Integer statut;

	private String lieu;

	@Column(name = "code_unite_ecclesiale")
	private Long codeUniteEcclesiale;

	@Column(name = "code_type_activite")
	private Long codeTypeActivite;
}
