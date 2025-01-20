package com.hoteling.project.domain.dto.response;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hoteling.project.common.ResponseCode;
import com.hoteling.project.common.ResponseMessage;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {

  private String code;
  private String message;

  public static ResponseEntity<ResponseDto> databaseError() {
    ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
  }

  public static ResponseEntity<ResponseDto> validationFailed() {
    ResponseDto responseBody = new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
  }

    public static void writeResponse(HttpServletResponse response, String code, String message, int status) throws IOException {
    response.setContentType("application/json");
    response.setStatus(status);
    String jsonResponse = "{ \"code\": \"" + code + "\", \"message\": \"" + message + "\" }";
    response.getWriter().write(jsonResponse);
  }

}
