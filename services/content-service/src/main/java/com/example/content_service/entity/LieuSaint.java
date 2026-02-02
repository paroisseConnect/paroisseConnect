package com.example.content_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lieux_saint")
@Getter
@Setter
@NoArgsConstructor
public class LieuSaint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	private String libelle;

	private String adresse;

	private String photo;

	@Column(name = "code_unite_ecclesiale")
	private Long codeUniteEcclesiale;
}
