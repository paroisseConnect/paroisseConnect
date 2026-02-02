package com.example.parish_service.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.parish_service.dto.UniteEcclesialeRequest;
import com.example.parish_service.entity.UniteEcclesiale;
import com.example.parish_service.repository.UniteEcclesialeRepository;

@Service
@Transactional
public class UniteEcclesialeService {

	private final UniteEcclesialeRepository repository;

	public UniteEcclesialeService(UniteEcclesialeRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<UniteEcclesiale> listAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public UniteEcclesiale get(Long code) {
		return repository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unite ecclesiale introuvable"));
	}

	public UniteEcclesiale create(UniteEcclesialeRequest request) {
		if (request.getCode() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code est obligatoire");
		}
		if (repository.existsById(request.getCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Code deja utilise");
		}
		UniteEcclesiale entity = new UniteEcclesiale();
		apply(entity, request);
		return repository.save(entity);
	}

	public UniteEcclesiale update(Long code, UniteEcclesialeRequest request) {
		UniteEcclesiale entity = get(code);
		apply(entity, request);
		entity.setCode(code);
		return repository.save(entity);
	}

	public void delete(Long code) {
		if (!repository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unite ecclesiale introuvable");
		}
		repository.deleteById(code);
	}

	private void apply(UniteEcclesiale entity, UniteEcclesialeRequest request) {
		entity.setCode(request.getCode() != null ? request.getCode() : entity.getCode());
		entity.setLibelle(request.getLibelle());
		entity.setDescription(request.getDescription());
		entity.setTitre(request.getTitre());
		entity.setAdresse(request.getAdresse());
		entity.setCodeType(request.getCodeType());
		entity.setLongitude(request.getLongitude());
		entity.setLatitude(request.getLatitude());
		entity.setAltitude(request.getAltitude());
		entity.setFidele(request.getFidele());
		entity.setDateCreationUnite(request.getDateCreationUnite());
		entity.setStatutCanonique(request.getStatutCanonique());
		entity.setCodeUniteParent(request.getCodeUniteParent());
		entity.setActivite(request.getActivite());
		entity.setDateCreation(request.getDateCreation());
		entity.setDateModification(request.getDateModification());
		entity.setCodeCreateur(request.getCodeCreateur());
		entity.setCodeModificateur(request.getCodeModificateur());
	}
}
