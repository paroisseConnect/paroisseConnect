package com.example.content_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "objets_sacres")
@Getter
@Setter
@NoArgsConstructor
public class ObjetSacre {

	@Id
	private Long code;

	private String libelle;

	private String description;

	private String photo;

	@Column(name = "date_creation")
	private String dateCreation;

	@Column(name = "date_modification")
	private String dateModification;
}
