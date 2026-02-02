package com.example.user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

	@Id
	private Integer code;

	private String libelle;

	@Column(name = "code_createur")
	private String codeCreateur;

	@Column(name = "code_modificateur")
	private String codeModificateur;

	@Column(name = "date_creation")
	private String dateCreation;

	@Column(name = "date_modification")
	private String dateModification;
}
