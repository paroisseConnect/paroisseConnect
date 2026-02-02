package com.example.user_service.dto;

import com.example.user_service.entity.Utilisateur;

public record UserDto(
		Integer code,
		String username,
		String noms,
		String prenoms,
		String dateNaissance,
		String contact1,
		String contact2,
		String addresse) {

	public static UserDto from(Utilisateur user) {
		return new UserDto(
				user.getCode(),
				user.getUsername(),
				user.getNoms(),
				user.getPrenoms(),
				user.getDateNaissance(),
				user.getContact1(),
				user.getContact2(),
				user.getAddresse());
	}
}
