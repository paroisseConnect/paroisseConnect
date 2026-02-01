package com.example.worship_service.dto;

import java.time.LocalDate;

public record HoraireMesseOccurrenceDto(
		Long id,
		Long codeProgramme,
		Long codeCelebration,
		Long codeUniteEcclesiale,
		String libelleUniteEcclesiale,
		String libelleCelebration,
		String codeTypeMesse,
		String heure,
		Integer jourSemaine,
		LocalDate date) {
}
