package com.example.worship_service.dto;

public record CelebrationDto(
		Long code,
		String libelle,
		String granularite,
		String codeHoraire,
		String codeType,
		Long codeUniteEcclesiale) {
}
