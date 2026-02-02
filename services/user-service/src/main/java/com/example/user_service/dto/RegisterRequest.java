package com.example.user_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

	private String username;
	private String noms;
	private String prenoms;
	private String dateNaissance;
	private String contact1;
	private String contact2;
	private String password;
	private String addresse;
}
