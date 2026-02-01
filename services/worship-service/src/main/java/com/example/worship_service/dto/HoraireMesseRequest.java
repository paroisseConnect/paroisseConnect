package com.example.worship_service.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HoraireMesseRequest {

	@NotNull
	private HoraireMesseResourceType resourceType;

	@Valid
	private CelebrationRequest celebration;

	@Valid
	private ProgrammeRequest programme;

	@Valid
	private ProgrammeLienRequest programmeLien;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class CelebrationRequest {
		private String libelle;
		private String granularite;
		private String codeHoraire;
		private String codeType;
		private Long codeUniteEcclesiale;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class ProgrammeRequest {
		private String libelle;
		private LocalDate dateDebut;
		private LocalDate dateFin;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class ProgrammeLienRequest {
		private Long codeProgramme;
		private Long codeCelebrationEucharistique;
		private String description;
		private Integer jourSemaine;
		private LocalDate dateException;
	}
}
