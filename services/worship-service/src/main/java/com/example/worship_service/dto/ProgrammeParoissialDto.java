package com.example.worship_service.dto;

import java.time.LocalDate;

public record ProgrammeParoissialDto(Long code, String libelle, LocalDate dateDebut, LocalDate dateFin) {
}
