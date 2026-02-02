package com.example.parish_service.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UniteEcclesialeRequest {

	private Long code;
	private String libelle;
	private String description;
	private String titre;
	private String adresse;
	private Long codeType;
	private String longitude;
	private String latitude;
	private String altitude;
	private String fidele;
	private LocalDateTime dateCreationUnite;
	private String statutCanonique;
	private Long codeUniteParent;
	private String activite;
	private LocalDateTime dateCreation;
	private LocalDateTime dateModification;
	private Long codeCreateur;
	private Long codeModificateur;
}
