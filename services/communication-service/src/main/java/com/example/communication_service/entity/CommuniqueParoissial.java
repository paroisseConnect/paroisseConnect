package com.example.communication_service.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "communiques_paroissiaux", schema = "paroisse_schema")
@Getter
@Setter
@NoArgsConstructor
public class CommuniqueParoissial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code")
	private Long code;

	@Column(name = "libelle")
	private String libelle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_type")
	private TypeCommunique type;

	@Column(name = "code_unite_ecclesiale")
	private Long codeUniteEcclesiale;

	@Column(name = "contenu", columnDefinition = "TEXT")
	private String contenu;

	@Column(name = "date_publication")
	private LocalDateTime datePublication;

	@Column(name = "date_debut_affichage")
	private LocalDateTime dateDebutAffichage;

	@Column(name = "date_fin_affichage")
	private LocalDateTime dateFinAffichage;

	@Column(name = "actif")
	private Boolean actif;

	@Column(name = "date_creation")
	private LocalDateTime dateCreation;

	@Column(name = "date_modification")
	private LocalDateTime dateModification;

	@Column(name = "code_createur")
	private Long codeCreateur;

	@Column(name = "code_modificateur")
	private Long codeModificateur;

	@OneToMany(mappedBy = "communique", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PieceJointeCommunique> piecesJointes = new ArrayList<>();

	@PrePersist
	void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		if (dateCreation == null) {
			dateCreation = now;
		}
		if (dateModification == null) {
			dateModification = dateCreation;
		}
		if (datePublication == null) {
			datePublication = dateCreation;
		}
		if (actif == null) {
			actif = Boolean.TRUE;
		}
	}

	@PreUpdate
	void onUpdate() {
		dateModification = LocalDateTime.now();
	}
}
