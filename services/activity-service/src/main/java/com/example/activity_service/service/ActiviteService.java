package com.example.activity_service.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.activity_service.dto.ActiviteRequest;
import com.example.activity_service.dto.TypeActiviteRequest;
import com.example.activity_service.entity.Activite;
import com.example.activity_service.entity.TypeActivite;
import com.example.activity_service.repository.ActiviteRepository;
import com.example.activity_service.repository.TypeActiviteRepository;

@Service
@Transactional
public class ActiviteService {

	private final ActiviteRepository activiteRepository;
	private final TypeActiviteRepository typeRepository;

	public ActiviteService(ActiviteRepository activiteRepository, TypeActiviteRepository typeRepository) {
		this.activiteRepository = activiteRepository;
		this.typeRepository = typeRepository;
	}

	@Transactional(readOnly = true)
	public List<Activite> listActivites() {
		return activiteRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Activite getActivite(Long code) {
		return activiteRepository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activite introuvable"));
	}

	public Activite createActivite(ActiviteRequest request) {
		if (request.getCode() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code est obligatoire");
		}
		if (activiteRepository.existsById(request.getCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Code deja utilise");
		}
		Activite entity = new Activite();
		apply(entity, request);
		return activiteRepository.save(entity);
	}

	public Activite updateActivite(Long code, ActiviteRequest request) {
		Activite entity = getActivite(code);
		apply(entity, request);
		entity.setCode(code);
		return activiteRepository.save(entity);
	}

	public void deleteActivite(Long code) {
		if (!activiteRepository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activite introuvable");
		}
		activiteRepository.deleteById(code);
	}

	@Transactional(readOnly = true)
	public List<TypeActivite> listTypes() {
		return typeRepository.findAll();
	}

	public TypeActivite createType(TypeActiviteRequest request) {
		if (request.getCode() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code est obligatoire");
		}
		if (typeRepository.existsById(request.getCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Code deja utilise");
		}
		TypeActivite entity = new TypeActivite();
		apply(entity, request);
		return typeRepository.save(entity);
	}

	public TypeActivite updateType(Long code, TypeActiviteRequest request) {
		TypeActivite entity = typeRepository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Type introuvable"));
		apply(entity, request);
		entity.setCode(code);
		return typeRepository.save(entity);
	}

	public void deleteType(Long code) {
		if (!typeRepository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Type introuvable");
		}
		typeRepository.deleteById(code);
	}

	private void apply(Activite entity, ActiviteRequest request) {
		entity.setCode(request.getCode() != null ? request.getCode() : entity.getCode());
		entity.setLibelle(request.getLibelle());
		entity.setDateActivite(request.getDateActivite());
		entity.setDescription(request.getDescription());
		entity.setFrequence(request.getFrequence());
		entity.setDateDebut(request.getDateDebut());
		entity.setDateFin(request.getDateFin());
		entity.setStatut(request.getStatut());
		entity.setLieu(request.getLieu());
		entity.setCodeUniteEcclesiale(request.getCodeUniteEcclesiale());
		entity.setCodeTypeActivite(request.getCodeTypeActivite());
	}

	private void apply(TypeActivite entity, TypeActiviteRequest request) {
		entity.setCode(request.getCode() != null ? request.getCode() : entity.getCode());
		entity.setLibelle(request.getLibelle());
		entity.setDescription(request.getDescription());
		entity.setDateCreation(request.getDateCreation());
		entity.setCodeCreateur(request.getCodeCreateur());
		entity.setCodeModificateur(request.getCodeModificateur());
	}
}
