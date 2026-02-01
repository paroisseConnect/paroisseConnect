package com.example.worship_service.entity;

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
@Table(name = "celebration_eucharistique", schema = "paroisse_schema")
@Getter
@Setter
@NoArgsConstructor
public class CelebrationEucharistique {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code")
	private Long code;

	@Column(name = "libelle")
	private String libelle;

	@Column(name = "granularite")
	private String granularite;

	@Column(name = "code_horaire")
	private String codeHoraire;

	@Column(name = "code_type")
	private String codeType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_unite_ecclesiale")
	private UniteEcclesiale uniteEcclesiale;
}
