package com.example.content_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ObjetSacreRequest {

	private Long code;
	private String libelle;
	private String description;
	private String photo;
	private String dateCreation;
	private String dateModification;
}
