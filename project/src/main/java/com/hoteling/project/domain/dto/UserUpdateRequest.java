package com.hoteling.project.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
	private String newUEmail;
	private String newUPhoneNum;
	private String uName;
	private String uSsn;
	private String emailCode;
}
