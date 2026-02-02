package com.example.worship_service.entity;

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
@Table(name = "intention_messe")
@Getter
@Setter
@NoArgsConstructor
public class IntentionMesse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	@Column(name = "intention_messe")
	private String intentionMesse;

	@Column(name = "code_celebration_eucharistique")
	private Long codeCelebrationEucharistique;

	@Column(name = "montant_verse")
	private String montantVerse;

	@Column(name = "date_versement")
	private String dateVersement;

	@Column(name = "date_creation")
	private String dateCreation;

	@Column(name = "date_modification")
	private String dateModification;

	@Column(name = "code_createur")
	private Long codeCreateur;

	@Column(name = "code_modificateur")
	private Long codeModificateur;
}
