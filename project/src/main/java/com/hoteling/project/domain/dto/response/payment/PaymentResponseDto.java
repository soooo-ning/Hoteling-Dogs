package com.hoteling.project.domain.dto.response.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hoteling.project.common.ResponseCode;
import com.hoteling.project.common.ResponseMessage;
import com.hoteling.project.domain.dto.response.ResponseDto;
import com.hoteling.project.domain.entity.PaymentEntity;

import lombok.Getter;

@Getter
public class PaymentResponseDto extends ResponseDto {

	private Long paymentId;
	private Long reservationId;
	private LocalDateTime paymentDate;
	private String paymentMethod;
	private BigDecimal amount;
	private String status;
	private String impUid;

	private PaymentResponseDto(PaymentEntity payment) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.paymentId = payment.getPaymentId();
		this.reservationId = payment.getReservation().getReservationId();
		this.paymentDate = payment.getPaymentDate();
		this.paymentMethod = payment.getPaymentMethod().toString();
		this.amount = payment.getPaymentPrice();
		this.status = payment.getStatus().toString();
		this.impUid = payment.getImpUid();
	}

	public static ResponseEntity<? super ResponseDto> success(PaymentEntity payment) {
		PaymentResponseDto responseBody = new PaymentResponseDto(payment);
		return ResponseEntity.ok(responseBody);
	}

	public static ResponseEntity<ResponseDto> notFound() {
		ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_PAYMENT, ResponseMessage.NOT_EXISTED_PAYMENT);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
	}

}
