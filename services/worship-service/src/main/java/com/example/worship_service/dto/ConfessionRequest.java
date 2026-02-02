package com.example.worship_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfessionRequest {

	private String lieu;
	private String horaireDebut;
	private String horaireFin;
	private String granularite;
}
