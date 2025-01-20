package com.hoteling.project.domain.dto.response.reservation;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hoteling.project.common.ResponseCode;
import com.hoteling.project.common.ResponseMessage;
import com.hoteling.project.domain.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class ReservationResponseDto extends ResponseDto {

	private Long reservationId;
	private BigDecimal totalAmount;

	private ReservationResponseDto(Long reservationId, BigDecimal totalAmount) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.reservationId = reservationId;
		this.totalAmount = totalAmount;
	}

	// 성공 response
	public static ResponseEntity<ResponseDto> success(Long reservationId, BigDecimal totalAmount) {
		ReservationResponseDto responseBody = new ReservationResponseDto(reservationId, totalAmount);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}

	// 실패 response
	public static ResponseEntity<ResponseDto> notExistUser() {
		ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

	public static ResponseEntity<ResponseDto> notExistReservation() {
		ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_RESERVATION, ResponseMessage.NOT_EXISTED_RESERVATION);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

	public static ResponseEntity<ResponseDto> reservationFail() {
		ResponseDto result = new ResponseDto(ResponseCode.RESERVATION_FAIL, ResponseMessage.RESERVATION_FAIL);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

	public static ResponseEntity<ResponseDto> roomNotAvailable() {
		ResponseDto result = new ResponseDto(ResponseCode.ROOM_NOT_AVAILABLE, ResponseMessage.ROOM_NOT_AVAILABLE);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

}
