package com.example.worship_service.dto;

import java.time.LocalDate;

public record ProgrammeLienDto(
		Long code,
		Long codeProgramme,
		Long codeCelebrationEucharistique,
		Integer jourSemaine,
		LocalDate dateException,
		String description) {
}
