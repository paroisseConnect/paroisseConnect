package com.example.activity_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_activite")
@Getter
@Setter
@NoArgsConstructor
public class TypeActivite {

	@Id
	private Long code;

	private String libelle;

	private String description;

	@Column(name = "date_creation")
	private String dateCreation;

	@Column(name = "code_createur")
	private Long codeCreateur;

	@Column(name = "code_modificateur")
	private Long codeModificateur;
}
