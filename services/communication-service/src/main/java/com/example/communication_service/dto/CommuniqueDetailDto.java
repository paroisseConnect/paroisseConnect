package com.example.communication_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CommuniqueDetailDto(
		Long code,
		String libelle,
		Long codeType,
		String libelleType,
		String contenu,
		LocalDateTime datePublication,
		LocalDateTime dateDebutAffichage,
		LocalDateTime dateFinAffichage,
		Long codeUniteEcclesiale,
		Boolean actif,
		List<PieceJointeDto> piecesJointes) {
}
