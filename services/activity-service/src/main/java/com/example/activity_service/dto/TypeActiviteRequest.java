package com.example.activity_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TypeActiviteRequest {

	private Long code;
	private String libelle;
	private String description;
	private String dateCreation;
	private Long codeCreateur;
	private Long codeModificateur;
}
