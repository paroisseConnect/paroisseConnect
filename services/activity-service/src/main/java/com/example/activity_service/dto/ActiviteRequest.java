package com.example.activity_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActiviteRequest {

	private Long code;
	private String libelle;
	private String dateActivite;
	private String description;
	private String frequence;
	private String dateDebut;
	private String dateFin;
	private Integer statut;
	private String lieu;
	private Long codeUniteEcclesiale;
	private Long codeTypeActivite;
}
