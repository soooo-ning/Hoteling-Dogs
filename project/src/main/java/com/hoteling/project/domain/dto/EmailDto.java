package com.hoteling.project.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailDto {
	
	// 이메일 주소
	private String mail;
	// 인증코드
	private String verifyCode;
	
}
