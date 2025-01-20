package com.hoteling.project.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LoginRequest {
	@JsonProperty("uId")
	private String uId;
	
	@JsonProperty("uPw")
	private String uPw;
}
