package com.example.communication_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommuniqueRequest {

	@NotBlank
	private String libelle;

	@NotNull
	private Long codeType;

	private Long codeUniteEcclesiale;
	private String contenu;
	private LocalDateTime datePublication;
	private LocalDateTime dateDebutAffichage;
	private LocalDateTime dateFinAffichage;
	private Boolean actif;

	@Valid
	private List<PieceJointeRequest> piecesJointes;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class PieceJointeRequest {
		private String type;
		private String url;
		private String nomFichier;
	}
}
