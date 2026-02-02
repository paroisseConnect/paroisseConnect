package com.example.user_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleRequest {

	private Integer code;
	private String libelle;
	private String codeCreateur;
	private String codeModificateur;
	private String dateCreation;
	private String dateModification;
}
