package com.example.communication_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "piece_jointe_communique", schema = "paroisse_schema")
@Getter
@Setter
@NoArgsConstructor
public class PieceJointeCommunique {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code")
	private Long code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_communique")
	private CommuniqueParoissial communique;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private TypePieceJointe type;

	@Column(name = "url", length = 500)
	private String url;

	@Column(name = "nom_fichier")
	private String nomFichier;
}
