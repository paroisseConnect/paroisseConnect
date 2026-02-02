package com.example.content_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.content_service.entity.LieuSaint;
import com.example.content_service.entity.ObjetSacre;
import com.example.content_service.service.ContentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Contenus spirituels")
public class ContentController {

	private final ContentService service;

	public ContentController(ContentService service) {
		this.service = service;
	}

	@GetMapping("/lieux-saints")
	@Operation(summary = "Lister les lieux spirituels")
	public List<LieuSaint> listLieux() {
		return service.listLieux();
	}

	@GetMapping("/lieux-saints/{code}")
	@Operation(summary = "Detail d'un lieu spirituel")
	public LieuSaint getLieu(@PathVariable Long code) {
		return service.getLieu(code);
	}

	@GetMapping("/objets-sacres")
	@Operation(summary = "Lister les objets sacres")
	public List<ObjetSacre> listObjets() {
		return service.listObjets();
	}

	@GetMapping("/objets-sacres/{code}")
	@Operation(summary = "Detail d'un objet sacre")
	public ObjetSacre getObjet(@PathVariable Long code) {
		return service.getObjet(code);
	}
}
