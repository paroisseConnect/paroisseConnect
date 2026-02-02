package com.example.user_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionRequest {

	private Integer code;
	private Integer codeUniteEcclesiale;
}
