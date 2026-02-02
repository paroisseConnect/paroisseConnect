package com.example.worship_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IntentionMesseRequest {

	private String intentionMesse;
	private Long codeCelebrationEucharistique;
	private String montantVerse;
	private String dateVersement;
	private String dateCreation;
	private String dateModification;
	private Long codeCreateur;
	private Long codeModificateur;
}
