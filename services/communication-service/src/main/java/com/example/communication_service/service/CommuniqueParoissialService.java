package com.example.communication_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.communication_service.dto.CommuniqueDetailDto;
import com.example.communication_service.dto.CommuniqueRequest;
import com.example.communication_service.dto.CommuniqueSummaryDto;
import com.example.communication_service.dto.PieceJointeDto;
import com.example.communication_service.dto.TypeCommuniqueDto;
import com.example.communication_service.entity.CommuniqueParoissial;
import com.example.communication_service.entity.PieceJointeCommunique;
import com.example.communication_service.entity.TypeCommunique;
import com.example.communication_service.entity.TypePieceJointe;
import com.example.communication_service.repository.CommuniqueParoissialRepository;
import com.example.communication_service.repository.TypeCommuniqueRepository;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class CommuniqueParoissialService {

	private final CommuniqueParoissialRepository communiqueRepository;
	private final TypeCommuniqueRepository typeRepository;

	public CommuniqueParoissialService(CommuniqueParoissialRepository communiqueRepository,
			TypeCommuniqueRepository typeRepository) {
		this.communiqueRepository = communiqueRepository;
		this.typeRepository = typeRepository;
	}

	@Transactional(readOnly = true)
	public List<TypeCommuniqueDto> listTypes() {
		return typeRepository.findAll().stream()
				.map(type -> new TypeCommuniqueDto(type.getCode(), type.getLibelle(), type.getDescription()))
				.toList();
	}

	@Transactional(readOnly = true)
	public Page<CommuniqueSummaryDto> listCommuniques(Long codeUniteEcclesiale,
			Long codeType,
			LocalDate dateDebut,
			LocalDate dateFin,
			Boolean actif,
			Pageable pageable) {
		Specification<CommuniqueParoissial> spec = (root, query, cb) -> {
			List<Predicate> predicates = new java.util.ArrayList<>();
			if (codeUniteEcclesiale != null) {
				predicates.add(cb.or(
						cb.isNull(root.get("codeUniteEcclesiale")),
						cb.equal(root.get("codeUniteEcclesiale"), codeUniteEcclesiale)));
			}
			if (codeType != null) {
				predicates.add(cb.equal(root.get("type").get("code"), codeType));
			}
			if (dateDebut != null) {
				LocalDateTime from = dateDebut.atStartOfDay();
				predicates.add(cb.greaterThanOrEqualTo(root.get("datePublication"), from));
			}
			if (dateFin != null) {
				LocalDateTime to = dateFin.atTime(LocalTime.MAX);
				predicates.add(cb.lessThanOrEqualTo(root.get("datePublication"), to));
			}
			Boolean actifValue = actif != null ? actif : Boolean.TRUE;
			predicates.add(cb.equal(root.get("actif"), actifValue));
			return cb.and(predicates.toArray(new Predicate[0]));
		};

		return communiqueRepository.findAll(spec, pageable)
				.map(this::toSummaryDto);
	}

	@Transactional(readOnly = true)
	public CommuniqueDetailDto getCommunique(Long id) {
		CommuniqueParoissial communique = communiqueRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Communique introuvable"));
		return toDetailDto(communique);
	}

	public CommuniqueDetailDto create(CommuniqueRequest request) {
		CommuniqueParoissial communique = new CommuniqueParoissial();
		applyRequest(communique, request);
		return toDetailDto(communiqueRepository.save(communique));
	}

	public CommuniqueDetailDto update(Long id, CommuniqueRequest request) {
		CommuniqueParoissial communique = communiqueRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Communique introuvable"));
		applyRequest(communique, request);
		return toDetailDto(communiqueRepository.save(communique));
	}

	public void delete(Long id) {
		if (!communiqueRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Communique introuvable");
		}
		communiqueRepository.deleteById(id);
	}

	public CommuniqueDetailDto addAttachment(Long communiqueId, CommuniqueRequest.PieceJointeRequest request) {
		CommuniqueParoissial communique = communiqueRepository.findById(communiqueId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Communique introuvable"));
		PieceJointeCommunique entity = new PieceJointeCommunique();
		entity.setCommunique(communique);
		entity.setNomFichier(request.getNomFichier());
		entity.setUrl(request.getUrl());
		entity.setType(parseTypePieceJointe(request.getType()));
		communique.getPiecesJointes().add(entity);
		return toDetailDto(communiqueRepository.save(communique));
	}

	public CommuniqueDetailDto removeAttachment(Long communiqueId, Long pieceJointeId) {
		CommuniqueParoissial communique = communiqueRepository.findById(communiqueId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Communique introuvable"));
		boolean removed = communique.getPiecesJointes().removeIf(piece -> pieceJointeId.equals(piece.getCode()));
		if (!removed) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Piece jointe introuvable");
		}
		return toDetailDto(communiqueRepository.save(communique));
	}

	public void triggerNotification(Long communiqueId) {
		if (!communiqueRepository.existsById(communiqueId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Communique introuvable");
		}
	}

	private void applyRequest(CommuniqueParoissial communique, CommuniqueRequest request) {
		communique.setLibelle(request.getLibelle());
		communique.setCodeUniteEcclesiale(request.getCodeUniteEcclesiale());
		communique.setContenu(request.getContenu());
		communique.setDatePublication(request.getDatePublication());
		communique.setDateDebutAffichage(request.getDateDebutAffichage());
		communique.setDateFinAffichage(request.getDateFinAffichage());
		if (request.getActif() != null) {
			communique.setActif(request.getActif());
		}

		TypeCommunique type = typeRepository.findById(request.getCodeType())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de communique invalide"));
		communique.setType(type);

		communique.getPiecesJointes().clear();
		if (request.getPiecesJointes() != null) {
			for (CommuniqueRequest.PieceJointeRequest piece : request.getPiecesJointes()) {
				PieceJointeCommunique entity = new PieceJointeCommunique();
				entity.setCommunique(communique);
				entity.setNomFichier(piece.getNomFichier());
				entity.setUrl(piece.getUrl());
				entity.setType(parseTypePieceJointe(piece.getType()));
				communique.getPiecesJointes().add(entity);
			}
		}
	}

	private CommuniqueSummaryDto toSummaryDto(CommuniqueParoissial communique) {
		TypeCommunique type = communique.getType();
		return new CommuniqueSummaryDto(
				communique.getCode(),
				communique.getLibelle(),
				type != null ? type.getCode() : null,
				type != null ? type.getLibelle() : null,
				communique.getDatePublication(),
				communique.getCodeUniteEcclesiale(),
				communique.getActif(),
				extrait(communique.getContenu()));
	}

	private CommuniqueDetailDto toDetailDto(CommuniqueParoissial communique) {
		TypeCommunique type = communique.getType();
		List<PieceJointeDto> pieces = communique.getPiecesJointes().stream()
				.map(piece -> new PieceJointeDto(
						piece.getCode(),
						piece.getType() != null ? piece.getType().name() : null,
						piece.getUrl(),
						piece.getNomFichier()))
				.toList();

		return new CommuniqueDetailDto(
				communique.getCode(),
				communique.getLibelle(),
				type != null ? type.getCode() : null,
				type != null ? type.getLibelle() : null,
				communique.getContenu(),
				communique.getDatePublication(),
				communique.getDateDebutAffichage(),
				communique.getDateFinAffichage(),
				communique.getCodeUniteEcclesiale(),
				communique.getActif(),
				pieces);
	}

	private String extrait(String contenu) {
		if (contenu == null) {
			return null;
		}
		String trimmed = contenu.trim();
		if (trimmed.length() <= 200) {
			return trimmed;
		}
		return trimmed.substring(0, 200);
	}

	private TypePieceJointe parseTypePieceJointe(String type) {
		if (type == null || type.isBlank()) {
			return null;
		}
		String normalized = type.trim().toUpperCase(Locale.ROOT);
		for (TypePieceJointe value : TypePieceJointe.values()) {
			if (Objects.equals(value.name(), normalized)) {
				return value;
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de piece jointe invalide");
	}
}
