package com.example.communication_service.dto;

import java.time.LocalDateTime;

public record CommuniqueSummaryDto(
		Long code,
		String libelle,
		Long codeType,
		String libelleType,
		LocalDateTime datePublication,
		Long codeUniteEcclesiale,
		Boolean actif,
		String extrait) {
}
