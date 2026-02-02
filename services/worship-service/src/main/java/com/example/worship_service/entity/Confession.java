package com.example.worship_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "confessions")
@Getter
@Setter
@NoArgsConstructor
public class Confession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	private String lieu;
	private String horaireDebut;
	private String horaireFin;
	private String granularite;
}
