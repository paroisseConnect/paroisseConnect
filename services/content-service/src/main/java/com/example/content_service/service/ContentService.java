package com.example.content_service.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.content_service.dto.LieuSaintRequest;
import com.example.content_service.dto.ObjetSacreRequest;
import com.example.content_service.entity.LieuSaint;
import com.example.content_service.entity.ObjetSacre;
import com.example.content_service.repository.LieuSaintRepository;
import com.example.content_service.repository.ObjetSacreRepository;

@Service
@Transactional
public class ContentService {

	private final LieuSaintRepository lieuRepository;
	private final ObjetSacreRepository objetRepository;

	public ContentService(LieuSaintRepository lieuRepository, ObjetSacreRepository objetRepository) {
		this.lieuRepository = lieuRepository;
		this.objetRepository = objetRepository;
	}

	@Transactional(readOnly = true)
	public List<LieuSaint> listLieux() {
		return lieuRepository.findAll();
	}

	@Transactional(readOnly = true)
	public LieuSaint getLieu(Long code) {
		return lieuRepository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu introuvable"));
	}

	public LieuSaint createLieu(LieuSaintRequest request) {
		LieuSaint entity = new LieuSaint();
		apply(entity, request);
		return lieuRepository.save(entity);
	}

	public LieuSaint updateLieu(Long code, LieuSaintRequest request) {
		LieuSaint entity = getLieu(code);
		apply(entity, request);
		entity.setCode(code);
		return lieuRepository.save(entity);
	}

	public void deleteLieu(Long code) {
		if (!lieuRepository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu introuvable");
		}
		lieuRepository.deleteById(code);
	}

	@Transactional(readOnly = true)
	public List<ObjetSacre> listObjets() {
		return objetRepository.findAll();
	}

	@Transactional(readOnly = true)
	public ObjetSacre getObjet(Long code) {
		return objetRepository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objet introuvable"));
	}

	public ObjetSacre createObjet(ObjetSacreRequest request) {
		if (request.getCode() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code est obligatoire");
		}
		if (objetRepository.existsById(request.getCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Code deja utilise");
		}
		ObjetSacre entity = new ObjetSacre();
		apply(entity, request);
		return objetRepository.save(entity);
	}

	public ObjetSacre updateObjet(Long code, ObjetSacreRequest request) {
		ObjetSacre entity = getObjet(code);
		apply(entity, request);
		entity.setCode(code);
		return objetRepository.save(entity);
	}

	public void deleteObjet(Long code) {
		if (!objetRepository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Objet introuvable");
		}
		objetRepository.deleteById(code);
	}

	private void apply(LieuSaint entity, LieuSaintRequest request) {
		if (request.getCode() != null) {
			entity.setCode(request.getCode());
		}
		entity.setLibelle(request.getLibelle());
		entity.setAdresse(request.getAdresse());
		entity.setPhoto(request.getPhoto());
		entity.setCodeUniteEcclesiale(request.getCodeUniteEcclesiale());
	}

	private void apply(ObjetSacre entity, ObjetSacreRequest request) {
		entity.setCode(request.getCode() != null ? request.getCode() : entity.getCode());
		entity.setLibelle(request.getLibelle());
		entity.setDescription(request.getDescription());
		entity.setPhoto(request.getPhoto());
		entity.setDateCreation(request.getDateCreation());
		entity.setDateModification(request.getDateModification());
	}
}
