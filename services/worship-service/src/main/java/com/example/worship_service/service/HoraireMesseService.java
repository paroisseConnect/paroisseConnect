package com.example.worship_service.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.worship_service.dto.CelebrationDto;
import com.example.worship_service.dto.HoraireMesseDetailDto;
import com.example.worship_service.dto.HoraireMesseOccurrenceDto;
import com.example.worship_service.dto.HoraireMesseRequest;
import com.example.worship_service.dto.HoraireMesseResourceType;
import com.example.worship_service.dto.ProgrammeLienDto;
import com.example.worship_service.dto.ProgrammeParoissialDto;
import com.example.worship_service.dto.TypeMesseDto;
import com.example.worship_service.dto.UniteEcclesialeDto;
import com.example.worship_service.entity.CelebrationEucharistique;
import com.example.worship_service.entity.ProgrammeCelebrationEucharistique;
import com.example.worship_service.entity.ProgrammeParoissial;
import com.example.worship_service.entity.UniteEcclesiale;
import com.example.worship_service.repository.CelebrationEucharistiqueRepository;
import com.example.worship_service.repository.ProgrammeCelebrationEucharistiqueRepository;
import com.example.worship_service.repository.ProgrammeParoissialRepository;
import com.example.worship_service.repository.UniteEcclesialeRepository;

@Service
@Transactional
public class HoraireMesseService {

	private static final DateTimeFormatter HORAIRE_FORMAT = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT);
	private static final DateTimeFormatter HORAIRE_FALLBACK = DateTimeFormatter.ofPattern("H:mm", Locale.ROOT);

	private final CelebrationEucharistiqueRepository celebrationRepository;
	private final ProgrammeParoissialRepository programmeRepository;
	private final ProgrammeCelebrationEucharistiqueRepository lienRepository;
	private final UniteEcclesialeRepository uniteRepository;

	public HoraireMesseService(CelebrationEucharistiqueRepository celebrationRepository,
			ProgrammeParoissialRepository programmeRepository,
			ProgrammeCelebrationEucharistiqueRepository lienRepository,
			UniteEcclesialeRepository uniteRepository) {
		this.celebrationRepository = celebrationRepository;
		this.programmeRepository = programmeRepository;
		this.lienRepository = lienRepository;
		this.uniteRepository = uniteRepository;
	}

	@Transactional(readOnly = true)
	public List<TypeMesseDto> listTypesMesse() {
		return celebrationRepository.findDistinctCodeTypes().stream()
				.filter(Objects::nonNull)
				.map(code -> new TypeMesseDto(code, code))
				.toList();
	}

	@Transactional(readOnly = true)
	public List<UniteEcclesialeDto> listParoisses() {
		return uniteRepository.findAll().stream()
				.map(unite -> new UniteEcclesialeDto(unite.getCode(), unite.getLibelle(), unite.getAdresse()))
				.toList();
	}

	@Transactional(readOnly = true)
	public Page<HoraireMesseOccurrenceDto> listHoraires(LocalDate date,
			Integer jourSemaine,
			LocalDate dateDebut,
			LocalDate dateFin,
			String codeTypeMesse,
			Long codeUniteEcclesiale,
			Pageable pageable) {
		List<ProgrammeCelebrationEucharistique> liens = lienRepository.findAll();
		List<HoraireMesseOccurrenceDto> occurrences = new ArrayList<>();
		for (ProgrammeCelebrationEucharistique lien : liens) {
			occurrences.addAll(buildOccurrences(lien, date, jourSemaine, dateDebut, dateFin, codeTypeMesse,
					codeUniteEcclesiale));
		}

		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), occurrences.size());
		List<HoraireMesseOccurrenceDto> pageContent = start > end ? List.of() : occurrences.subList(start, end);

		return new PageImpl<>(pageContent, pageable, occurrences.size());
	}

	@Transactional(readOnly = true)
	public HoraireMesseDetailDto getHoraire(Long id, LocalDate date) {
		ProgrammeCelebrationEucharistique lien = lienRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Horaire introuvable"));
		List<HoraireMesseOccurrenceDto> occurrences = buildOccurrences(lien, date, null, null, null, null, null);
		if (date != null && occurrences.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Occurrence introuvable");
		}
		HoraireMesseOccurrenceDto occurrence = occurrences.isEmpty() ? null : occurrences.getFirst();

		return toDetailDto(lien, occurrence);
	}

	public Object create(HoraireMesseRequest request) {
		return switch (request.getResourceType()) {
			case CELEBRATION -> createCelebration(request);
			case PROGRAMME -> createProgramme(request);
			case PROGRAMME_LIEN -> createProgrammeLien(request);
		};
	}

	public Object update(Long id, HoraireMesseRequest request) {
		return switch (request.getResourceType()) {
			case CELEBRATION -> updateCelebration(id, request);
			case PROGRAMME -> updateProgramme(id, request);
			case PROGRAMME_LIEN -> updateProgrammeLien(id, request);
		};
	}

	public void delete(Long id, HoraireMesseResourceType resourceType) {
		switch (resourceType) {
			case CELEBRATION -> celebrationRepository.deleteById(id);
			case PROGRAMME -> programmeRepository.deleteById(id);
			case PROGRAMME_LIEN -> lienRepository.deleteById(id);
		}
	}

	private CelebrationDto createCelebration(HoraireMesseRequest request) {
		HoraireMesseRequest.CelebrationRequest payload = requireCelebration(request);
		CelebrationEucharistique celebration = new CelebrationEucharistique();
		applyCelebrationPayload(celebration, payload);
		return toCelebrationDto(celebrationRepository.save(celebration));
	}

	private CelebrationDto updateCelebration(Long id, HoraireMesseRequest request) {
		HoraireMesseRequest.CelebrationRequest payload = requireCelebration(request);
		CelebrationEucharistique celebration = celebrationRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Celebration introuvable"));
		applyCelebrationPayload(celebration, payload);
		return toCelebrationDto(celebrationRepository.save(celebration));
	}

	private void applyCelebrationPayload(CelebrationEucharistique celebration,
			HoraireMesseRequest.CelebrationRequest payload) {
		celebration.setLibelle(payload.getLibelle());
		celebration.setGranularite(payload.getGranularite());
		celebration.setCodeHoraire(payload.getCodeHoraire());
		celebration.setCodeType(payload.getCodeType());
		if (payload.getCodeUniteEcclesiale() != null) {
			UniteEcclesiale unite = uniteRepository.findById(payload.getCodeUniteEcclesiale())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unite ecclesiale invalide"));
			celebration.setUniteEcclesiale(unite);
		} else {
			celebration.setUniteEcclesiale(null);
		}
	}

	private ProgrammeParoissialDto createProgramme(HoraireMesseRequest request) {
		HoraireMesseRequest.ProgrammeRequest payload = requireProgramme(request);
		ProgrammeParoissial programme = new ProgrammeParoissial();
		applyProgrammePayload(programme, payload);
		return toProgrammeDto(programmeRepository.save(programme));
	}

	private ProgrammeParoissialDto updateProgramme(Long id, HoraireMesseRequest request) {
		HoraireMesseRequest.ProgrammeRequest payload = requireProgramme(request);
		ProgrammeParoissial programme = programmeRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Programme introuvable"));
		applyProgrammePayload(programme, payload);
		return toProgrammeDto(programmeRepository.save(programme));
	}

	private void applyProgrammePayload(ProgrammeParoissial programme, HoraireMesseRequest.ProgrammeRequest payload) {
		programme.setLibelle(payload.getLibelle());
		programme.setDateDebut(payload.getDateDebut());
		programme.setDateFin(payload.getDateFin());
	}

	private ProgrammeLienDto createProgrammeLien(HoraireMesseRequest request) {
		HoraireMesseRequest.ProgrammeLienRequest payload = requireProgrammeLien(request);
		ProgrammeCelebrationEucharistique lien = new ProgrammeCelebrationEucharistique();
		applyProgrammeLienPayload(lien, payload);
		return toProgrammeLienDto(lienRepository.save(lien));
	}

	private ProgrammeLienDto updateProgrammeLien(Long id, HoraireMesseRequest request) {
		HoraireMesseRequest.ProgrammeLienRequest payload = requireProgrammeLien(request);
		ProgrammeCelebrationEucharistique lien = lienRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lien introuvable"));
		applyProgrammeLienPayload(lien, payload);
		return toProgrammeLienDto(lienRepository.save(lien));
	}

	private void applyProgrammeLienPayload(ProgrammeCelebrationEucharistique lien,
			HoraireMesseRequest.ProgrammeLienRequest payload) {
		ProgrammeParoissial programme = programmeRepository.findById(payload.getCodeProgramme())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Programme invalide"));
		CelebrationEucharistique celebration = celebrationRepository.findById(payload.getCodeCelebrationEucharistique())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Celebration invalide"));
		lien.setProgramme(programme);
		lien.setCelebration(celebration);
		lien.setDescription(payload.getDescription());
		lien.setJourSemaine(payload.getJourSemaine());
		lien.setDateException(payload.getDateException());
		if (lien.getDateCreation() == null) {
			lien.setDateCreation(LocalDateTime.now());
		}
		lien.setDateModification(LocalDateTime.now());
	}

	private List<HoraireMesseOccurrenceDto> buildOccurrences(ProgrammeCelebrationEucharistique lien,
			LocalDate date,
			Integer jourSemaine,
			LocalDate dateDebut,
			LocalDate dateFin,
			String codeTypeMesse,
			Long codeUniteEcclesiale) {
		CelebrationEucharistique celebration = lien.getCelebration();
		if (celebration == null) {
			return List.of();
		}
		if (codeTypeMesse != null && !Objects.equals(codeTypeMesse, celebration.getCodeType())) {
			return List.of();
		}
		UniteEcclesiale unite = celebration.getUniteEcclesiale();
		if (codeUniteEcclesiale != null && (unite == null || !Objects.equals(codeUniteEcclesiale, unite.getCode()))) {
			return List.of();
		}

		LocalDate programmeDebut = Optional.ofNullable(lien.getProgramme())
				.map(ProgrammeParoissial::getDateDebut)
				.orElse(null);
		LocalDate programmeFin = Optional.ofNullable(lien.getProgramme())
				.map(ProgrammeParoissial::getDateFin)
				.orElse(null);

		LocalDate requestStart = date != null ? date : dateDebut;
		LocalDate requestEnd = date != null ? date : dateFin;
		if (requestStart == null && requestEnd == null) {
			requestStart = programmeDebut;
			requestEnd = programmeFin;
		}
		if (requestStart == null || requestEnd == null) {
			return List.of();
		}

		LocalDate start = maxDate(programmeDebut, requestStart);
		LocalDate end = minDate(programmeFin, requestEnd);
		if (start != null && end != null && start.isAfter(end)) {
			return List.of();
		}

		if (lien.getDateException() != null) {
			LocalDate exceptionDate = lien.getDateException();
			if (!isWithinRange(exceptionDate, start, end)) {
				return List.of();
			}
			if (jourSemaine != null && !Objects.equals(jourSemaine, exceptionDate.getDayOfWeek().getValue())) {
				return List.of();
			}
			return List.of(buildOccurrence(lien, celebration, unite, exceptionDate));
		}

		Integer lienJour = lien.getJourSemaine();
		if (jourSemaine != null && lienJour != null && !Objects.equals(jourSemaine, lienJour)) {
			return List.of();
		}
		if (lienJour == null) {
			if (date != null) {
				return List.of(buildOccurrence(lien, celebration, unite, date));
			}
			return List.of();
		}

		DayOfWeek targetDay = DayOfWeek.of(lienJour);
		LocalDate current = adjustToNext(start, targetDay);
		List<HoraireMesseOccurrenceDto> occurrences = new ArrayList<>();
		while (current != null && !current.isAfter(end)) {
			occurrences.add(buildOccurrence(lien, celebration, unite, current));
			current = current.plusWeeks(1);
		}
		return occurrences;
	}

	private HoraireMesseOccurrenceDto buildOccurrence(ProgrammeCelebrationEucharistique lien,
			CelebrationEucharistique celebration,
			UniteEcclesiale unite,
			LocalDate date) {
		return new HoraireMesseOccurrenceDto(
				lien.getCode(),
				lien.getProgramme() != null ? lien.getProgramme().getCode() : null,
				celebration.getCode(),
				unite != null ? unite.getCode() : null,
				unite != null ? unite.getLibelle() : null,
				celebration.getLibelle(),
				celebration.getCodeType(),
				normalizeHoraire(celebration.getCodeHoraire()),
				lien.getJourSemaine(),
				date);
	}

	private HoraireMesseDetailDto toDetailDto(ProgrammeCelebrationEucharistique lien,
			HoraireMesseOccurrenceDto occurrence) {
		CelebrationEucharistique celebration = lien.getCelebration();
		UniteEcclesiale unite = celebration != null ? celebration.getUniteEcclesiale() : null;
		return new HoraireMesseDetailDto(
				lien.getCode(),
				lien.getProgramme() != null ? lien.getProgramme().getCode() : null,
				celebration != null ? celebration.getCode() : null,
				unite != null ? unite.getCode() : null,
				unite != null ? unite.getLibelle() : null,
				celebration != null ? celebration.getLibelle() : null,
				celebration != null ? celebration.getCodeType() : null,
				celebration != null ? normalizeHoraire(celebration.getCodeHoraire()) : null,
				lien.getJourSemaine(),
				occurrence != null ? occurrence.date() : lien.getDateException(),
				lien.getDescription());
	}

	private CelebrationDto toCelebrationDto(CelebrationEucharistique celebration) {
		return new CelebrationDto(
				celebration.getCode(),
				celebration.getLibelle(),
				celebration.getGranularite(),
				celebration.getCodeHoraire(),
				celebration.getCodeType(),
				celebration.getUniteEcclesiale() != null ? celebration.getUniteEcclesiale().getCode() : null);
	}

	private ProgrammeParoissialDto toProgrammeDto(ProgrammeParoissial programme) {
		return new ProgrammeParoissialDto(
				programme.getCode(),
				programme.getLibelle(),
				programme.getDateDebut(),
				programme.getDateFin());
	}

	private ProgrammeLienDto toProgrammeLienDto(ProgrammeCelebrationEucharistique lien) {
		return new ProgrammeLienDto(
				lien.getCode(),
				lien.getProgramme() != null ? lien.getProgramme().getCode() : null,
				lien.getCelebration() != null ? lien.getCelebration().getCode() : null,
				lien.getJourSemaine(),
				lien.getDateException(),
				lien.getDescription());
	}

	private HoraireMesseRequest.CelebrationRequest requireCelebration(HoraireMesseRequest request) {
		if (request.getCelebration() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charge utile celebration manquante");
		}
		return request.getCelebration();
	}

	private HoraireMesseRequest.ProgrammeRequest requireProgramme(HoraireMesseRequest request) {
		if (request.getProgramme() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charge utile programme manquante");
		}
		return request.getProgramme();
	}

	private HoraireMesseRequest.ProgrammeLienRequest requireProgrammeLien(HoraireMesseRequest request) {
		if (request.getProgrammeLien() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Charge utile lien manquante");
		}
		if (request.getProgrammeLien().getCodeProgramme() == null
				|| request.getProgrammeLien().getCodeCelebrationEucharistique() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Programme ou celebration manquant");
		}
		return request.getProgrammeLien();
	}

	private String normalizeHoraire(String codeHoraire) {
		if (codeHoraire == null || codeHoraire.isBlank()) {
			return null;
		}
		try {
			LocalTime time = LocalTime.parse(codeHoraire, HORAIRE_FORMAT);
			return time.format(HORAIRE_FORMAT);
		} catch (DateTimeParseException ignored) {
			try {
				LocalTime time = LocalTime.parse(codeHoraire, HORAIRE_FALLBACK);
				return time.format(HORAIRE_FORMAT);
			} catch (DateTimeParseException ignoredAgain) {
				return codeHoraire;
			}
		}
	}

	private LocalDate maxDate(LocalDate first, LocalDate second) {
		if (first == null) {
			return second;
		}
		if (second == null) {
			return first;
		}
		return first.isAfter(second) ? first : second;
	}

	private LocalDate minDate(LocalDate first, LocalDate second) {
		if (first == null) {
			return second;
		}
		if (second == null) {
			return first;
		}
		return first.isBefore(second) ? first : second;
	}

	private boolean isWithinRange(LocalDate date, LocalDate start, LocalDate end) {
		if (date == null) {
			return false;
		}
		if (start != null && date.isBefore(start)) {
			return false;
		}
		if (end != null && date.isAfter(end)) {
			return false;
		}
		return true;
	}

	private LocalDate adjustToNext(LocalDate start, DayOfWeek targetDay) {
		if (start == null || targetDay == null) {
			return null;
		}
		LocalDate current = start;
		int diff = targetDay.getValue() - current.getDayOfWeek().getValue();
		if (diff < 0) {
			diff += 7;
		}
		return current.plusDays(diff);
	}
}
