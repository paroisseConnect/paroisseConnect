package com.example.content_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LieuSaintRequest {

	private Long code;
	private String libelle;
	private String adresse;
	private String photo;
	private Long codeUniteEcclesiale;
}
