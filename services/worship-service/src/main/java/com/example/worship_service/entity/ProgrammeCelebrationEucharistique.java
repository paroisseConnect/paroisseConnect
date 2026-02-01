package com.example.worship_service.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "programme_celebration_eucharistique", schema = "paroisse_schema")
@Getter
@Setter
@NoArgsConstructor
public class ProgrammeCelebrationEucharistique {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code")
	private Long code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_programme")
	private ProgrammeParoissial programme;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_celebration_eucharistique")
	private CelebrationEucharistique celebration;

	@Column(name = "description")
	private String description;

	@Column(name = "jour_semaine")
	private Integer jourSemaine;

	@Column(name = "date_exception")
	private LocalDate dateException;

	@Column(name = "code_createur")
	private Long codeCreateur;

	@Column(name = "code_modificateur")
	private Long codeModificateur;

	@Column(name = "date_creation")
	private LocalDateTime dateCreation;

	@Column(name = "date_modification")
	private LocalDateTime dateModification;
}
