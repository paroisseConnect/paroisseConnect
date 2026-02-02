package com.example.worship_service.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.worship_service.dto.ConfessionRequest;
import com.example.worship_service.dto.IntentionMesseRequest;
import com.example.worship_service.entity.Confession;
import com.example.worship_service.entity.IntentionMesse;
import com.example.worship_service.repository.ConfessionRepository;
import com.example.worship_service.repository.IntentionMesseRepository;

@Service
@Transactional
public class SacrementService {

	private final ConfessionRepository confessionRepository;
	private final IntentionMesseRepository intentionRepository;

	public SacrementService(ConfessionRepository confessionRepository,
			IntentionMesseRepository intentionRepository) {
		this.confessionRepository = confessionRepository;
		this.intentionRepository = intentionRepository;
	}

	@Transactional(readOnly = true)
	public List<Confession> listConfessions() {
		return confessionRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Confession getConfession(Long code) {
		return confessionRepository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Confession introuvable"));
	}

	public Confession createConfession(ConfessionRequest request) {
		Confession confession = new Confession();
		apply(confession, request);
		return confessionRepository.save(confession);
	}

	public Confession updateConfession(Long code, ConfessionRequest request) {
		Confession confession = getConfession(code);
		apply(confession, request);
		confession.setCode(code);
		return confessionRepository.save(confession);
	}

	public void deleteConfession(Long code) {
		if (!confessionRepository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Confession introuvable");
		}
		confessionRepository.deleteById(code);
	}

	@Transactional(readOnly = true)
	public List<IntentionMesse> listIntentions() {
		return intentionRepository.findAll();
	}

	@Transactional(readOnly = true)
	public IntentionMesse getIntention(Long code) {
		return intentionRepository.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Intention introuvable"));
	}

	public IntentionMesse createIntention(IntentionMesseRequest request) {
		IntentionMesse intention = new IntentionMesse();
		apply(intention, request);
		return intentionRepository.save(intention);
	}

	public IntentionMesse updateIntention(Long code, IntentionMesseRequest request) {
		IntentionMesse intention = getIntention(code);
		apply(intention, request);
		intention.setCode(code);
		return intentionRepository.save(intention);
	}

	public void deleteIntention(Long code) {
		if (!intentionRepository.existsById(code)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Intention introuvable");
		}
		intentionRepository.deleteById(code);
	}

	private void apply(Confession confession, ConfessionRequest request) {
		confession.setLieu(request.getLieu());
		confession.setHoraireDebut(request.getHoraireDebut());
		confession.setHoraireFin(request.getHoraireFin());
		confession.setGranularite(request.getGranularite());
	}

	private void apply(IntentionMesse intention, IntentionMesseRequest request) {
		intention.setIntentionMesse(request.getIntentionMesse());
		intention.setCodeCelebrationEucharistique(request.getCodeCelebrationEucharistique());
		intention.setMontantVerse(request.getMontantVerse());
		intention.setDateVersement(request.getDateVersement());
		intention.setDateCreation(request.getDateCreation());
		intention.setDateModification(request.getDateModification());
		intention.setCodeCreateur(request.getCodeCreateur());
		intention.setCodeModificateur(request.getCodeModificateur());
	}
}
