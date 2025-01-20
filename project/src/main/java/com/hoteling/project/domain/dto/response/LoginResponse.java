package com.hoteling.project.domain.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hoteling.project.common.ResponseCode;
import com.hoteling.project.common.ResponseMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends ResponseDto {
    private String token;    // JWT 토큰
    private int expirationTime; // 토큰 만료 시간

    public LoginResponse(String token) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.token = token;
        this.expirationTime = 3600;
      }

      public static ResponseEntity<LoginResponse> success(String token) {
    	  LoginResponse result = new LoginResponse(token);
        return ResponseEntity.status(HttpStatus.OK).body(result);
      }

      public static ResponseEntity<LoginResponse> signInFail() {
    	// 실패 응답을 LoginResponse로 반환
    	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new LoginResponse(null)); // 여기서 token을 null로 설정
      }
      
      
}

